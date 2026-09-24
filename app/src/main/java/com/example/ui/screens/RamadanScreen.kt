package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.local.RamadanFastRecord
import com.example.data.prayer.PrayerTimeCalculator
import com.example.data.preferences.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanScreen(onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
    val db = remember { WafaApplication.instance.database }
    val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    val todayCalendar = remember(currentTimeMillis) {
        Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
    }
    val todayStr = remember(currentTimeMillis) {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(todayCalendar.time)
    }

    val todayFastRecord by db.ramadanDao().getRecordForDate(todayStr).collectAsStateWithLifecycle(initialValue = null)
    val allFastRecords by db.ramadanDao().getAllFastRecords().collectAsStateWithLifecycle(initialValue = emptyList())

    val prayerTimes = remember(userPrefs, todayCalendar) {
        PrayerTimeCalculator.calculate(
            calendar = todayCalendar,
            latitude = userPrefs.latitude,
            longitude = userPrefs.longitude,
            calculationMethod = userPrefs.calculationMethod,
            asrMethod = userPrefs.asrMethod,
            is24Hour = userPrefs.is24Hour
        )
    }

    // Sehri is 10 mins before Fajr, Iftar is Maghrib
    val sehriTime = prayerTimes.fajr
    val iftarTime = prayerTimes.maghrib

    val isFastedToday = todayFastRecord?.isFasted == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("রমজানুল মুবারক ড্যাশবোর্ড", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Sehri & Iftar Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "আজকের সেহরি ও ইফতারের সময়সূচি",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "সেহরি শেষ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = sehriTime,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(50.dp)
                                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "ইফতারের সময়",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = iftarTime,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Fasting tracker toggle button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    db.ramadanDao().saveRecord(
                                        RamadanFastRecord(
                                            date = todayStr,
                                            isFasted = !isFastedToday
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFastedToday) Color(0xFF198754) else MaterialTheme.colorScheme.secondary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = if (isFastedToday) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isFastedToday) "আজকের রোজা রাখা সম্পন্ন হয়েছে" else "আজকের রোজা মার্ক করুন")
                        }
                    }
                }
            }

            // Fasting summary stats
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val totalFasted = allFastRecords.count { it.isFasted }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$totalFasted দিন", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(text = "মোট রোজা পূর্ণ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "৩০ দিন", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(text = "রমজানের লক্ষ্য", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // Ramadan Duas Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "রোজার নিয়ত ও ইফতারের দোয়া",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "১. রোজার নিয়তের বাক্য:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = "نَوَيْتُ أَنْ أَصُومَ غَدًا مِنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللَّهُ",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "উচ্চারণ: নাওয়াইতু আন আসূমা গাদান মিন শাহরি রামাদানাল মুবারাকি ফারদান লাকা ইয়া আল্লাহ।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "অর্থ: হে আল্লাহ! আমি আপনার সন্তুষ্টির উদ্দেশ্যে পবিত্র রমজানের আগামীকালের ফরজ রোজা রাখার নিয়ত করলাম।",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "২. ইফতারের দোয়া:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "উচ্চারণ: যাহাবায জামা'উ ওয়াবতাল্লাতিল উরূকু ওয়া ছাবাতাল আজরু ইনশাআল্লাহ।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "অর্থ: পিপাসা দূর হলো, শিরা-উপশিরা সিক্ত হলো এবং আল্লাহর ইচ্ছায় সওয়াব নির্ধারিত হলো। (আবু দাউদ: ২৩৫৭)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
