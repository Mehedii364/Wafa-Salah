package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.prayer.CityData
import com.example.data.preferences.UserPreferences
import com.example.receiver.PrayerReminderReceiver
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
    val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    var showCitySelector by remember { mutableStateOf(false) }
    var showCalculationMethodSelector by remember { mutableStateOf(false) }
    var showAsrMethodSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("সেটিংস ও পছন্দসমূহ", fontWeight = FontWeight.Bold) },
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
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Language & Appearance
            item {
                Text(
                    text = "ভাষা ও ইন্টারফেস থিম",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("অ্যাপের ভাষা", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = userPrefs.language == "bn",
                                    onClick = { coroutineScope.launch { prefsRepo.updateLanguage("bn") } },
                                    label = { Text("বাংলা") }
                                )
                                FilterChip(
                                    selected = userPrefs.language == "en",
                                    onClick = { coroutineScope.launch { prefsRepo.updateLanguage("en") } },
                                    label = { Text("English") }
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("থিম মোড", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FilterChip(
                                    selected = userPrefs.themeMode == "system",
                                    onClick = { coroutineScope.launch { prefsRepo.updateTheme("system") } },
                                    label = { Text("সিস্টেম") }
                                )
                                FilterChip(
                                    selected = userPrefs.themeMode == "light",
                                    onClick = { coroutineScope.launch { prefsRepo.updateTheme("light") } },
                                    label = { Text("লাইট") }
                                )
                                FilterChip(
                                    selected = userPrefs.themeMode == "dark",
                                    onClick = { coroutineScope.launch { prefsRepo.updateTheme("dark") } },
                                    label = { Text("ডার্ক") }
                                )
                            }
                        }
                    }
                }
            }

            // 2. Prayer Calculation & Location
            item {
                Text(
                    text = "নামাজের সময় হিসাব ও অবস্থান",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCitySelector = true }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("বর্তমান অবস্থান / শহর", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text(userPrefs.cityName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCalculationMethodSelector = true }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("হিসাব পদ্ধতি (Calculation Method)", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                val methodDesc = when (userPrefs.calculationMethod) {
                                    "KARACHI" -> "ইসলামিক বিজ্ঞান বিশ্ববিদ্যালয়, করাচি (সাব-কন্টিনেন্ট)"
                                    "MWL" -> "মুসলিম ওয়ার্ল্ড লিগ (MWL)"
                                    "ISNA" -> "ইসলামিক সোসাইটি অব নর্থ আমেরিকা (ISNA)"
                                    "MAKKAH" -> "উম্মুল কুরা ইউনিভার্সিটি, মক্কা"
                                    else -> "মিশরীয় জেনারেল অথরিটি"
                                }
                                Text(methodDesc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAsrMethodSelector = true }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("আসর নামাজের জুহুর ছায়া পদ্ধতি", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text(if (userPrefs.asrMethod == "HANAFI") "হানাফী (দ্বিগুণ ছায়া)" else "শাফেয়ী / সাধারণ (একগুণ ছায়া)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // 3. Notifications & Azan
            item {
                Text(
                    text = "স্মার্ট আজান ও নোটিফিকেশন",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("আজানের সুর / টোন বাজান", style = MaterialTheme.typography.bodyLarge)
                            Switch(
                                checked = userPrefs.azanSoundEnabled,
                                onCheckedChange = { coroutineScope.launch { prefsRepo.updateAzanSound(it) } }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("ভাইব্রেশন / কম্পন", style = MaterialTheme.typography.bodyLarge)
                            Switch(
                                checked = userPrefs.vibrateEnabled,
                                onCheckedChange = { coroutineScope.launch { prefsRepo.updateVibrate(it) } }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Per prayer toggles
                        val prayers = listOf(
                            "Fajr" to ("ফজর" to userPrefs.notifyFajr),
                            "Dhuhr" to ("যোহর" to userPrefs.notifyDhuhr),
                            "Asr" to ("আসর" to userPrefs.notifyAsr),
                            "Maghrib" to ("মাগরিব" to userPrefs.notifyMaghrib),
                            "Isha" to ("ইশা" to userPrefs.notifyIsha)
                        )

                        prayers.forEach { (nameEn, pair) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${pair.first} নামাজের এলার্ট", style = MaterialTheme.typography.bodyMedium)
                                Switch(
                                    checked = pair.second,
                                    onCheckedChange = { coroutineScope.launch { prefsRepo.updatePrayerNotification(nameEn, it) } }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                PrayerReminderReceiver.triggerNotification(
                                    context = context,
                                    prayerName = "Maghrib",
                                    prayerTime = "সন্ধ্যা ০৬:১০",
                                    isAzan = true,
                                    vibrate = userPrefs.vibrateEnabled
                                )
                            }
                        ) {
                            Icon(Icons.Filled.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("আজান নোটিফিকেশন টেস্ট করুন")
                        }
                    }
                }
            }

            // 4. Floating Widget Option
            item {
                Text(
                    text = "ফ্লোটিং প্রেয়ার উইজেট (ঐচ্ছিক)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("স্ক্রিনে ফ্লোটিং উইজেট দেখান", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                Text("অন্যান্য অ্যাপ ব্যবহারকালেও নামাজের ওয়াক্ত ও কাউন্টডাউন সহজে দেখতে সাহায্য করে।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Switch(
                                checked = userPrefs.floatingWidgetEnabled,
                                onCheckedChange = { enabled ->
                                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                                        val intent = Intent(
                                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:${context.packageName}")
                                        )
                                        context.startActivity(intent)
                                    } else {
                                        coroutineScope.launch { prefsRepo.updateFloatingWidget(enabled) }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // City selector modal
    if (showCitySelector) {
        ModalBottomSheet(onDismissRequest = { showCitySelector = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text("শহর নির্বাচন করুন", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    items(CityData.CITIES.size) { index ->
                        val city = CityData.CITIES[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        prefsRepo.updateLocation("${city.nameBn}, ${city.country}", city.latitude, city.longitude)
                                        showCitySelector = false
                                    }
                                }
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${city.nameBn} (${city.nameEn})", fontWeight = FontWeight.Bold)
                            Text(city.country, color = MaterialTheme.colorScheme.outline)
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }

    // Calculation method modal
    if (showCalculationMethodSelector) {
        ModalBottomSheet(onDismissRequest = { showCalculationMethodSelector = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text("হিসাব পদ্ধতি নির্বাচন করুন", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                val methods = listOf(
                    "KARACHI" to "করাচি (Islamic Sciences Karachi - ১৮° ফজর ও ইশা)",
                    "MWL" to "মুসলিম ওয়ার্ল্ড লিগ (Muslim World League - ১৮°/১৭°)",
                    "ISNA" to "উত্তর আমেরিকা (ISNA - ১৫°/১৫°)",
                    "MAKKAH" to "উম্মুল কুরা, মক্কা (১৮.৫° ফজর / মাগরিবের ৯০ মি. পর ইশা)",
                    "EGYPT" to "মিশরীয় সাধারণ কর্তৃপক্ষ (Egyptian Authority - ১৯.৫°/১৭.৫°)"
                )
                methods.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    prefsRepo.updateCalculationMethod(key)
                                    showCalculationMethodSelector = false
                                }
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                        if (userPrefs.calculationMethod == key) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }

    // Asr method modal
    if (showAsrMethodSelector) {
        ModalBottomSheet(onDismissRequest = { showAsrMethodSelector = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Text("আসর নামাজের সময় পদ্ধতি", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                val asrMethods = listOf(
                    "HANAFI" to "হানাফী মাযহাব (বস্তুর ছায়া দ্বিগুণ হলে)",
                    "STANDARD" to "শাফেয়ী, মালিকি, হাম্বলী ও সাধারণ (বস্তুর ছায়া একগুণ হলে)"
                )
                asrMethods.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    prefsRepo.updateAsrMethod(key)
                                    showAsrMethodSelector = false
                                }
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                        if (userPrefs.asrMethod == key) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
