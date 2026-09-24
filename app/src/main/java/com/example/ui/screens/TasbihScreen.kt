package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.local.TasbihRecord
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { WafaApplication.instance.database }
    val historyRecords by db.tasbihDao().getAllRecords().collectAsStateWithLifecycle(initialValue = emptyList())

    val presetZikrs = listOf(
        "سُبْحَانَ اللَّهِ" to "সুবহানাল্লাহ (আল্লাহ অতি পবিত্র)",
        "الْحَمْدُ لِلَّهِ" to "আলহামদুলিল্লাহ (সমস্ত প্রশংসা আল্লাহর)",
        "اللَّهُ أَكْبَرُ" to "আল্লাহু আকবার (আল্লাহ সর্বশ্রেষ্ঠ)",
        "لَا إِلَٰهَ إِلَّا اللَّهُ" to "লা ইলাহা ইল্লাল্লাহ (আল্লাহ ছাড়া উপাস্য নেই)",
        "أَسْتَغْفِرُ اللَّهَ" to "আস্তাগফিরুল্লাহ (আল্লাহর কাছে ক্ষমা চাই)",
        "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ" to "সুবহানাল্লাহি ওয়া বিহামদিহী"
    )

    var selectedZikirIndex by remember { mutableIntStateOf(0) }
    var currentCount by remember { mutableIntStateOf(0) }
    var targetCount by remember { mutableIntStateOf(33) }
    var lapCount by remember { mutableIntStateOf(0) }
    var vibrateEnabled by remember { mutableStateOf(true) }
    var showHistoryDialog by remember { mutableStateOf(false) }

    fun triggerHaptic(strong: Boolean = false) {
        if (!vibrateEnabled) return
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val duration = if (strong) 150L else 30L
                val amp = if (strong) VibrationEffect.DEFAULT_AMPLITUDE else 80
                vibrator?.vibrate(VibrationEffect.createOneShot(duration, amp))
            } else {
                vibrator?.vibrate(if (strong) 150L else 30L)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onCountTap() {
        val next = currentCount + 1
        if (next >= targetCount) {
            triggerHaptic(strong = true)
            currentCount = 0
            lapCount += 1
            // Save to Room DB
            coroutineScope.launch {
                db.tasbihDao().insertRecord(
                    TasbihRecord(
                        zikirText = presetZikrs[selectedZikirIndex].first,
                        count = targetCount,
                        target = targetCount
                    )
                )
            }
        } else {
            triggerHaptic(strong = false)
            currentCount = next
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Zikir Selector & Target Row
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "যিকির নির্বাচন করুন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { showHistoryDialog = true }) {
                        Icon(Icons.Filled.History, contentDescription = "History", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = presetZikrs[selectedZikirIndex].first,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = presetZikrs[selectedZikirIndex].second,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Target selector chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(33, 99, 100).forEach { target ->
                        FilterChip(
                            selected = targetCount == target,
                            onClick = {
                                targetCount = target
                                currentCount = 0
                            },
                            label = { Text("$target বার") }
                        )
                    }
                }
            }
        }

        // Giant Circular Touch Counter Bead
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onCountTap() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentCount",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp, fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "লক্ষ্য: $targetCount (চক্র: $lapCount)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "ট্যাপ করুন",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Bottom Controls: Reset, Change Zikir, Vibration
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        currentCount = 0
                        lapCount = 0
                    }
                ) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = "Reset", tint = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = {
                        selectedZikirIndex = (selectedZikirIndex + 1) % presetZikrs.size
                        currentCount = 0
                    }
                ) {
                    Icon(Icons.Filled.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("পরবর্তী যিকির")
                }

                IconButton(
                    onClick = { vibrateEnabled = !vibrateEnabled }
                ) {
                    Icon(
                        imageVector = if (vibrateEnabled) Icons.Filled.Vibration else Icons.Filled.Smartphone,
                        contentDescription = "Vibration",
                        tint = if (vibrateEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }

    // History Bottom Sheet
    if (showHistoryDialog) {
        ModalBottomSheet(onDismissRequest = { showHistoryDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "তাসবিহ পাঠের ইতিহাস",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(14.dp))

                if (historyRecords.isEmpty()) {
                    Text(
                        text = "এখনও কোনো ইতিহাস সংরক্ষিত হয়নি",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(historyRecords) { rec ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = rec.zikirText,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "সম্পন্ন: ${rec.count} বার",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
