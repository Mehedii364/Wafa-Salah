package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.local.PrayerRecord
import com.example.ui.theme.ColorPrayerCompleted
import com.example.ui.theme.ColorPrayerMissed
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SalahTrackerScreen() {
    val coroutineScope = rememberCoroutineScope()
    val db = remember { WafaApplication.instance.database }
    val allHistory by db.prayerDao().getAllHistory().collectAsStateWithLifecycle(initialValue = emptyList())

    val todayCalendar = remember { Calendar.getInstance() }
    val todayStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(todayCalendar.time) }

    val todayRecords by db.prayerDao().getRecordsForDate(todayStr).collectAsStateWithLifecycle(initialValue = emptyList())

    // Weekly statistics: last 7 days
    val last7Days = remember {
        val list = mutableListOf<String>()
        val cal = Calendar.getInstance()
        for (i in 0 until 7) {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
            list.add(dateStr)
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
        list.reversed()
    }

    val totalCompletedCount by db.prayerDao().getTotalCompletedCount().collectAsStateWithLifecycle(initialValue = 0)

    val todayCompletedCount = todayRecords.count { it.status == "COMPLETED" }

    // Calculate streak
    val streakDays = remember(allHistory) {
        var streak = 0
        val cal = Calendar.getInstance()
        for (i in 0..30) {
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
            val dayRecords = allHistory.filter { it.date == dateStr && it.status == "COMPLETED" }
            if (dayRecords.size >= 4) { // 4 or 5 prayers considered maintaining streak
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                if (i == 0) {
                    // today might still be in progress
                    cal.add(Calendar.DAY_OF_YEAR, -1)
                } else {
                    break
                }
            }
        }
        streak
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Today's Progress Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "আজকের সালাত অগ্রগতি",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$todayCompletedCount / ৫ ওয়াক্ত সম্পন্ন",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "${(todayCompletedCount * 20)}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { todayCompletedCount / 5f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                }
            }
        }

        // Streak & Total Summary Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$streakDays দিন",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "বর্তমান স্ট্রিক",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Total",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$totalCompletedCount ওয়াক্ত",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "মোট সম্পন্ন সালাত",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // Today's 5 Prayers Tracker with Direct Checkbox Toggles
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "আজকের ওয়াক্তসমূহ আপডেট করুন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val prayers = listOf(
                        "Fajr" to "ফজর",
                        "Dhuhr" to "যোহর",
                        "Asr" to "আসর",
                        "Maghrib" to "মাগরিব",
                        "Isha" to "ইশা"
                    )

                    prayers.forEach { (nameEn, nameBn) ->
                        val record = todayRecords.find { it.prayerName == nameEn }
                        val status = record?.status ?: "PENDING"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$nameBn ($nameEn)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = status == "COMPLETED",
                                    onClick = {
                                        coroutineScope.launch {
                                            db.prayerDao().insertOrUpdate(
                                                PrayerRecord(
                                                    date = todayStr,
                                                    prayerName = nameEn,
                                                    status = "COMPLETED"
                                                )
                                            )
                                        }
                                    },
                                    label = { Text("আদায়") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ColorPrayerCompleted,
                                        selectedLabelColor = MaterialTheme.colorScheme.surface
                                    )
                                )

                                FilterChip(
                                    selected = status == "MISSED",
                                    onClick = {
                                        coroutineScope.launch {
                                            db.prayerDao().insertOrUpdate(
                                                PrayerRecord(
                                                    date = todayStr,
                                                    prayerName = nameEn,
                                                    status = "MISSED"
                                                )
                                            )
                                        }
                                    },
                                    label = { Text("কাজা") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ColorPrayerMissed,
                                        selectedLabelColor = MaterialTheme.colorScheme.surface
                                    )
                                )
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }
        }

        // Weekly Statistics (7 Days)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "গত ৭ দিনের সাপ্তাহিক পরিসংখ্যান",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        last7Days.forEach { dateStr ->
                            val dayCount = allHistory.count { it.date == dateStr && it.status == "COMPLETED" }
                            val dayLabel = try {
                                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
                                SimpleDateFormat("E", Locale.getDefault()).format(date!!)
                            } catch (e: Exception) {
                                ""
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$dayCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height((dayCount * 18 + 12).dp)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(if (dayCount >= 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dayLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
