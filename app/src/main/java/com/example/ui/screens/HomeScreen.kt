package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.content.HadithRepository
import com.example.data.dua.DuaRepository
import com.example.data.local.PrayerRecord
import com.example.data.prayer.CityData
import com.example.data.prayer.PrayerTimeCalculator
import com.example.data.preferences.UserPreferences
import com.example.data.quran.QuranRepository
import com.example.service.AudioPlayerManager
import com.example.service.AudioTrack
import com.example.ui.components.ShareCardDialog
import com.example.ui.theme.ColorPrayerCompleted
import com.example.ui.theme.ColorPrayerMissed
import com.example.ui.theme.ColorPrayerPending
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToQuran: (Int) -> Unit,
    onNavigateToDua: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToSalahTracker: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
    val db = remember { WafaApplication.instance.database }
    val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    // Current time ticker for countdown
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    val todayStr = remember(currentTimeMillis) {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(currentTimeMillis))
    }

    val prayerRecords by db.prayerDao().getRecordsForDate(todayStr).collectAsStateWithLifecycle(initialValue = emptyList())

    val calendar = remember(currentTimeMillis) {
        Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
    }

    val prayerTimes = remember(userPrefs, calendar) {
        PrayerTimeCalculator.calculate(
            calendar = calendar,
            latitude = userPrefs.latitude,
            longitude = userPrefs.longitude,
            calculationMethod = userPrefs.calculationMethod,
            asrMethod = userPrefs.asrMethod,
            is24Hour = userPrefs.is24Hour
        )
    }

    // Share card state
    var shareCardData by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    var showCitySelector by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header with Location & Hijri Date
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showCitySelector = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = userPrefs.cityName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Change location",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = prayerTimes.dateGregorianFormatted,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = prayerTimes.dateHijriFormatted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // 2. Hero Next Prayer & Countdown Card
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val nextPrayerBn = when (prayerTimes.nextPrayerName) {
                        "Fajr" -> "ফজর"
                        "Sunrise" -> "সূর্যোদয়"
                        "Dhuhr" -> "যোহর"
                        "Asr" -> "আসর"
                        "Maghrib" -> "মাগরিব"
                        "Isha" -> "ইশা"
                        else -> prayerTimes.nextPrayerName
                    }

                    Text(
                        text = "পরবর্তী নামাজ: $nextPrayerBn",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = prayerTimes.nextPrayerTime,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp, fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Live countdown badge
                    val countdownSec = (prayerTimes.countdownMillis / 1000).toInt()
                    val hours = countdownSec / 3600
                    val minutes = (countdownSec % 3600) / 60
                    val seconds = countdownSec % 60
                    val countdownFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "বাকি আছে: $countdownFormatted",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        // 3. Quick Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Filled.MenuBook,
                    title = "আল-কুরআন",
                    onClick = { onNavigateToQuran(1) }
                )
                QuickActionButton(
                    icon = Icons.Filled.Explore,
                    title = "কিবলা কম্পাস",
                    onClick = onNavigateToQibla
                )
                QuickActionButton(
                    icon = Icons.Filled.Fingerprint,
                    title = "ডিজিটাল তাসবিহ",
                    onClick = onNavigateToTasbih
                )
                QuickActionButton(
                    icon = Icons.Filled.FactCheck,
                    title = "সালাত রেকর্ড",
                    onClick = onNavigateToSalahTracker
                )
            }
        }

        // 4. Daily Salah Tracker Quick Mark Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "আজকের সালাত ট্র্যাকার",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val completedCount = prayerRecords.count { it.status == "COMPLETED" }
                        Text(
                            text = "$completedCount/৫ ওয়াক্ত আদায়",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val prayers = listOf(
                        "Fajr" to "ফজর",
                        "Dhuhr" to "যোহর",
                        "Asr" to "আসর",
                        "Maghrib" to "মাগরিব",
                        "Isha" to "ইশা"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        prayers.forEach { (nameEn, nameBn) ->
                            val record = prayerRecords.find { it.prayerName == nameEn }
                            val status = record?.status ?: "PENDING"

                            val (statusColor, statusIcon) = when (status) {
                                "COMPLETED" -> ColorPrayerCompleted to Icons.Filled.CheckCircle
                                "MISSED" -> ColorPrayerMissed to Icons.Filled.Cancel
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f) to Icons.Outlined.RadioButtonUnchecked
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        coroutineScope.launch {
                                            val nextStatus = when (status) {
                                                "PENDING" -> "COMPLETED"
                                                "COMPLETED" -> "MISSED"
                                                else -> "PENDING"
                                            }
                                            db.prayerDao().insertOrUpdate(
                                                PrayerRecord(
                                                    date = todayStr,
                                                    prayerName = nameEn,
                                                    status = nextStatus
                                                )
                                            )
                                        }
                                    }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = statusIcon,
                                    contentDescription = nameBn,
                                    tint = statusColor,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nameBn,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Today's Full Prayer Timetable Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "আজকের নামাজের সময়সূচি",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PrayerTimeRow("ফজর", "Fajr", prayerTimes.fajr, prayerTimes.nextPrayerName == "Fajr")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    PrayerTimeRow("সূর্যোদয়", "Sunrise", prayerTimes.sunrise, false)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    PrayerTimeRow("যোহর", "Dhuhr", prayerTimes.dhuhr, prayerTimes.nextPrayerName == "Dhuhr")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    PrayerTimeRow("আসর", "Asr", prayerTimes.asr, prayerTimes.nextPrayerName == "Asr")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    PrayerTimeRow("মাগরিব", "Maghrib", prayerTimes.maghrib, prayerTimes.nextPrayerName == "Maghrib")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                    PrayerTimeRow("ইশা", "Isha", prayerTimes.isha, prayerTimes.nextPrayerName == "Isha")
                }
            }
        }

        // 6. Daily Quran Ayah (Ayatul Kursi with the 4 layers)
        if (userPrefs.showDailyAyah) {
            item {
                val dailyAyah = QuranRepository.getDailyAyah()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.MenuBook,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "দৈনিক কুরআনের আয়াত",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row {
                                IconButton(
                                    onClick = {
                                        AudioPlayerManager.play(
                                            context,
                                            AudioTrack(
                                                title = "আয়াতুল কুরসী (সূরা আল-বাকারা ২৫৫)",
                                                subtitle = "মিশারি রাশিদ আল-আফাসী",
                                                url = dailyAyah.audioUrl
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayCircle,
                                        contentDescription = "Play Audio",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        shareCardData = Triple(
                                            "আয়াতুল কুরসী (সূরা আল-বাকারা: ২৫৫)",
                                            dailyAyah.arabic,
                                            dailyAyah.banglaMeaning
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Layer 1: Arabic
                        Text(
                            text = dailyAyah.arabic,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp, lineHeight = 34.sp),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Layer 2: Bangla Pronunciation
                        Text(
                            text = "উচ্চারণ: ${dailyAyah.banglaPronunciation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Layer 4: Bangla Meaning
                        Text(
                            text = "অর্থ: ${dailyAyah.banglaMeaning}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "সূরা আল-বাকারা, আয়াত ২৫৫",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 7. Daily Dua Card
        if (userPrefs.showDailyDua) {
            item {
                val dailyDua = DuaRepository.getDailyDua()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.VolunteerActivism,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "দৈনিক দোয়া",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            IconButton(
                                onClick = {
                                    shareCardData = Triple(
                                        dailyDua.titleBn,
                                        dailyDua.arabic,
                                        dailyDua.banglaMeaning
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Text(
                            text = dailyDua.titleBn,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dailyDua.arabic,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, lineHeight = 28.sp),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = dailyDua.banglaPronunciation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = dailyDua.banglaMeaning,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "রেফারেন্স: ${dailyDua.reference}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // 8. Daily Hadith Card
        if (userPrefs.showDailyHadith) {
            item {
                val dailyHadith = HadithRepository.getDailyHadith()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.LibraryBooks,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "আজকের হাদীস",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            IconButton(
                                onClick = {
                                    shareCardData = Triple(
                                        dailyHadith.topic,
                                        dailyHadith.textArabic,
                                        dailyHadith.textBn
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Text(
                            text = dailyHadith.narratorBn,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dailyHadith.textArabic,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, lineHeight = 26.sp),
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = dailyHadith.textBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "সূত্র: ${dailyHadith.source}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    // City Selector Bottom Sheet
    if (showCitySelector) {
        ModalBottomSheet(onDismissRequest = { showCitySelector = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "আপনার শহর নির্বাচন করুন",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CityData.CITIES.size) { index ->
                        val city = CityData.CITIES[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        prefsRepo.updateLocation(
                                            "${city.nameBn}, ${city.country}",
                                            city.latitude,
                                            city.longitude
                                        )
                                        showCitySelector = false
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${city.nameBn} (${city.nameEn})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = city.country,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                if (userPrefs.cityName.contains(city.nameBn) || userPrefs.cityName.contains(city.nameEn)) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Share Card Dialog
    shareCardData?.let { (title, arabic, meaning) ->
        ShareCardDialog(
            title = title,
            arabicText = arabic,
            banglaText = meaning,
            reference = "Wafa Salah Tracker",
            onDismiss = { shareCardData = null }
        )
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PrayerTimeRow(
    nameBn: String,
    nameEn: String,
    time: String,
    isNext: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isNext) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isNext) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = "$nameBn ($nameEn)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = time,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isNext) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
