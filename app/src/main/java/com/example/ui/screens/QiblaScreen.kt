package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.preferences.UserPreferences
import kotlin.math.*

@Composable
fun QiblaScreen() {
    val context = LocalContext.current
    val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
    val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    val kaabaLat = 21.422487
    val kaabaLng = 39.826206

    // Calculate Qibla bearing from user latitude/longitude
    val qiblaBearing = remember(userPrefs.latitude, userPrefs.longitude) {
        val lat1 = Math.toRadians(userPrefs.latitude)
        val lat2 = Math.toRadians(kaabaLat)
        val deltaLng = Math.toRadians(kaabaLng - userPrefs.longitude)

        val y = sin(deltaLng)
        val x = cos(lat1) * tan(lat2) - sin(lat1) * cos(deltaLng)
        val qiblaRad = atan2(y, x)
        val qiblaDeg = (Math.toDegrees(qiblaRad) + 360.0) % 360.0
        qiblaDeg.toFloat()
    }

    // Distance to Kaaba in KM
    val distanceKm = remember(userPrefs.latitude, userPrefs.longitude) {
        val earthRadius = 6371.0
        val dLat = Math.toRadians(kaabaLat - userPrefs.latitude)
        val dLng = Math.toRadians(kaabaLng - userPrefs.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(userPrefs.latitude)) * cos(Math.toRadians(kaabaLat)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        (earthRadius * c).roundToInt()
    }

    var azimuth by remember { mutableFloatStateOf(0f) }
    var sensorAccuracy by remember { mutableIntStateOf(SensorManager.SENSOR_STATUS_ACCURACY_HIGH) }
    var hasCompassSensor by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (rotationSensor == null && (accelerometer == null || magnetometer == null)) {
            hasCompassSensor = false
        }

        var lastGravity: FloatArray? = null
        var lastGeomagnetic: FloatArray? = null

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val deg = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    azimuth = (deg + 360f) % 360f
                } else {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        lastGravity = event.values.clone()
                    }
                    if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        lastGeomagnetic = event.values.clone()
                    }
                    if (lastGravity != null && lastGeomagnetic != null) {
                        val r = FloatArray(9)
                        val i = FloatArray(9)
                        if (SensorManager.getRotationMatrix(r, i, lastGravity, lastGeomagnetic)) {
                            val actualOrientation = FloatArray(3)
                            SensorManager.getOrientation(r, actualOrientation)
                            val deg = Math.toDegrees(actualOrientation[0].toDouble()).toFloat()
                            azimuth = (deg + 360f) % 360f
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                sensorAccuracy = accuracy
            }
        }

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            accelerometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
            magnetometer?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val animatedAzimuth by animateFloatAsState(targetValue = azimuth, label = "azimuth")

    // Check if user is pointing accurately towards Qibla (+/- 3 degrees)
    val angleDifference = abs((azimuth - qiblaBearing + 540) % 360 - 180)
    val isAligned = angleDifference < 4.0f

    // Subtle vibration on alignment
    var wasAligned by remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            try {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator?.vibrate(50)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        wasAligned = isAligned
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Location & Qibla info header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = userPrefs.cityName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "কাবা শরীফের দূরত্ব: $distanceKm কিমি",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${qiblaBearing.roundToInt()}°",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (!hasCompassSensor) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = "⚠️ আপনার ডিভাইসে ম্যাগনেটিক কম্পাস সেন্সর পাওয়া যায়নি। আনুমানিক কিবলা কোণ: ${qiblaBearing.roundToInt()}° উত্তর-পূর্ব।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Animated Compass Dial
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(
                    if (isAligned) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
                .border(
                    width = if (isAligned) 4.dp else 2.dp,
                    color = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Rotating Compass Rose
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - 16.dp.toPx()

                // Rotate canvas with current phone azimuth
                rotate(-animatedAzimuth, center) {
                    // Draw compass marks
                    for (i in 0 until 360 step 30) {
                        val angleRad = Math.toRadians(i.toDouble())
                        val startX = center.x + (radius - 10.dp.toPx()) * sin(angleRad).toFloat()
                        val startY = center.y - (radius - 10.dp.toPx()) * cos(angleRad).toFloat()
                        val endX = center.x + radius * sin(angleRad).toFloat()
                        val endY = center.y - radius * cos(angleRad).toFloat()
                        drawLine(
                            color = if (i == 0) Color.Red else Color.Gray,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = if (i % 90 == 0) 3.dp.toPx() else 1.5.dp.toPx()
                        )
                    }

                    // Draw Qibla pointer on the dial
                    val qiblaRad = Math.toRadians(qiblaBearing.toDouble())
                    val qiblaPointerX = center.x + (radius - 28.dp.toPx()) * sin(qiblaRad).toFloat()
                    val qiblaPointerY = center.y - (radius - 28.dp.toPx()) * cos(qiblaRad).toFloat()

                    drawCircle(
                        color = Color(0xFFC59B27), // Gold Kaaba marker
                        radius = 12.dp.toPx(),
                        center = Offset(qiblaPointerX, qiblaPointerY)
                    )
                }

                // Center fixed needle
                drawCircle(
                    color = if (isAligned) Color(0xFF198754) else Color(0xFF0F5132),
                    radius = 8.dp.toPx(),
                    center = center
                )
            }

            // Fixed phone direction arrow (Points straight up)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Explore,
                    contentDescription = "Direction",
                    tint = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Center Alignment Badge
            if (isAligned) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "কিবলামুখী 🕌",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Status & Calibration Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "কম্পাস কোণ: ${azimuth.roundToInt()}° • কিবলা কোণ: ${qiblaBearing.roundToInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                val accuracyText = when (sensorAccuracy) {
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "উচ্চ নির্ভুলতা (High Accuracy)"
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "মাঝারি নির্ভুলতা"
                    else -> "ক্যালিব্রেশন প্রয়োজন: ফোনটিকে '8' এর মত ঘোরান"
                }
                Text(
                    text = accuracyText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
