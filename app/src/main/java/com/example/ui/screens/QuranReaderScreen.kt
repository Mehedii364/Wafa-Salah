package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.WafaApplication
import com.example.data.local.LastReadPosition
import com.example.data.local.QuranBookmark
import com.example.data.preferences.UserPreferences
import com.example.data.quran.Ayah
import com.example.data.quran.QuranRepository
import com.example.service.AudioPlayerManager
import com.example.service.AudioTrack
import com.example.ui.components.ShareCardDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surahNumber: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefsRepo = remember { WafaApplication.instance.preferencesRepository }
    val db = remember { WafaApplication.instance.database }
    val userPrefs by prefsRepo.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    val surah = remember(surahNumber) {
        QuranRepository.ALL_114_SURAHS.find { it.number == surahNumber } ?: QuranRepository.ALL_114_SURAHS[0]
    }
    val ayahs = remember(surahNumber) {
        QuranRepository.getAyahsForSurah(surahNumber)
    }

    // Save last read position automatically on launch
    LaunchedEffect(surahNumber) {
        db.quranDao().saveLastRead(
            LastReadPosition(
                surahNumber = surah.number,
                ayahNumber = 1,
                surahName = surah.nameBangla
            )
        )
    }

    var showSettingsSheet by remember { mutableStateOf(false) }
    var shareCardData by remember { mutableStateOf<Triple<String, String, String>?>(null) }
    val bookmarks by db.quranDao().getAllBookmarks().collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "${surah.number}. ${surah.nameBangla} (${surah.nameArabic})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${if (surah.revelationType == "Meccan") "মাক্কী" else "মাদানী"} • ${surah.totalAyahs} আয়াত",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val firstAudio = ayahs.firstOrNull { it.audioUrl.isNotEmpty() }?.audioUrl
                                ?: "https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3"
                            AudioPlayerManager.play(
                                context,
                                AudioTrack(
                                    title = "সূরা ${surah.nameBangla}",
                                    subtitle = "মিশারি রাশিদ আল-আফাসী",
                                    url = firstAudio,
                                    surahNumber = surah.number
                                )
                            )
                        }
                    ) {
                        Icon(Icons.Filled.PlayCircle, contentDescription = "Play Surah Audio", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showSettingsSheet = true }) {
                        Icon(Icons.Filled.Tune, contentDescription = "Reading Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Surah Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = surah.nameArabic,
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${surah.nameBangla} — ${surah.banglaMeaning}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${surah.nameEnglish} (${surah.englishMeaning})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Quick Layer Controls Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LayerFilterChip(
                        label = "আরবি",
                        selected = userPrefs.showArabic,
                        onClick = {
                            coroutineScope.launch {
                                prefsRepo.updateQuranLayers(!userPrefs.showArabic, userPrefs.showBanglaPronunciation, userPrefs.showEnglishMeaning, userPrefs.showBanglaMeaning)
                            }
                        }
                    )
                    LayerFilterChip(
                        label = "উচ্চারণ",
                        selected = userPrefs.showBanglaPronunciation,
                        onClick = {
                            coroutineScope.launch {
                                prefsRepo.updateQuranLayers(userPrefs.showArabic, !userPrefs.showBanglaPronunciation, userPrefs.showEnglishMeaning, userPrefs.showBanglaMeaning)
                            }
                        }
                    )
                    LayerFilterChip(
                        label = "অর্থ (বাংলা)",
                        selected = userPrefs.showBanglaMeaning,
                        onClick = {
                            coroutineScope.launch {
                                prefsRepo.updateQuranLayers(userPrefs.showArabic, userPrefs.showBanglaPronunciation, userPrefs.showEnglishMeaning, !userPrefs.showBanglaMeaning)
                            }
                        }
                    )
                    LayerFilterChip(
                        label = "English",
                        selected = userPrefs.showEnglishMeaning,
                        onClick = {
                            coroutineScope.launch {
                                prefsRepo.updateQuranLayers(userPrefs.showArabic, userPrefs.showBanglaPronunciation, !userPrefs.showEnglishMeaning, userPrefs.showBanglaMeaning)
                            }
                        }
                    )
                }
            }

            // Ayahs
            items(ayahs, key = { it.numberInSurah }) { ayah ->
                val isBookmarked = bookmarks.any { it.surahNumber == surah.number && it.ayahNumber == ayah.numberInSurah }

                AyahCard(
                    surahNumber = surah.number,
                    surahName = surah.nameBangla,
                    ayah = ayah,
                    userPrefs = userPrefs,
                    isBookmarked = isBookmarked,
                    onBookmarkToggle = {
                        coroutineScope.launch {
                            if (isBookmarked) {
                                db.quranDao().removeBookmark(surah.number, ayah.numberInSurah)
                            } else {
                                db.quranDao().insertBookmark(
                                    QuranBookmark(
                                        surahNumber = surah.number,
                                        ayahNumber = ayah.numberInSurah,
                                        surahName = surah.nameBangla,
                                        ayahTextArabic = ayah.arabic
                                    )
                                )
                            }
                        }
                    },
                    onPlayAudio = {
                        if (ayah.audioUrl.isNotEmpty()) {
                            AudioPlayerManager.play(
                                context,
                                AudioTrack(
                                    title = "সূরা ${surah.nameBangla}: আয়াত ${ayah.numberInSurah}",
                                    subtitle = "মিশারি রাশিদ আল-আফাসী",
                                    url = ayah.audioUrl,
                                    surahNumber = surah.number,
                                    ayahNumber = ayah.numberInSurah
                                )
                            )
                        }
                    },
                    onShare = {
                        shareCardData = Triple(
                            "সূরা ${surah.nameBangla} - আয়াত ${ayah.numberInSurah}",
                            ayah.arabic,
                            ayah.banglaMeaning
                        )
                    }
                )
            }
        }
    }

    // Reading Settings Sheet
    if (showSettingsSheet) {
        ModalBottomSheet(onDismissRequest = { showSettingsSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "কুরআন পঠন সেটিংস",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Arabic font size slider
                Text(
                    text = "আরবি হরফের আকার: ${userPrefs.arabicFontSize.toInt()}sp",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = userPrefs.arabicFontSize,
                    onValueChange = {
                        coroutineScope.launch {
                            prefsRepo.updateQuranFontSizes(it, userPrefs.banglaFontSize, userPrefs.englishFontSize)
                        }
                    },
                    valueRange = 18f..40f
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bangla font size slider
                Text(
                    text = "বাংলা হরফের আকার: ${userPrefs.banglaFontSize.toInt()}sp",
                    style = MaterialTheme.typography.titleMedium
                )
                Slider(
                    value = userPrefs.banglaFontSize,
                    onValueChange = {
                        coroutineScope.launch {
                            prefsRepo.updateQuranFontSizes(userPrefs.arabicFontSize, it, userPrefs.englishFontSize)
                        }
                    },
                    valueRange = 12f..24f
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showSettingsSheet = false }
                ) {
                    Text("সম্পন্ন")
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
            reference = "আল-কুরআন • Wafa Salah Tracker",
            onDismiss = { shareCardData = null }
        )
    }
}

@Composable
fun LayerFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun AyahCard(
    surahNumber: Int,
    surahName: String,
    ayah: Ayah,
    userPrefs: UserPreferences,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onPlayAudio: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Ayah Top Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ayah badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "$surahNumber:${ayah.numberInSurah}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row {
                    if (ayah.audioUrl.isNotEmpty()) {
                        IconButton(onClick = onPlayAudio) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play Audio",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onBookmarkToggle) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. LAYER 1: ARABIC (Original Arabic text)
            if (userPrefs.showArabic && ayah.arabic.isNotBlank()) {
                Text(
                    text = "ARABIC",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ayah.arabic,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = userPrefs.arabicFontSize.sp,
                        lineHeight = (userPrefs.arabicFontSize * 1.6f).sp
                    ),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 2. LAYER 2: বাংলা উচ্চারণ (Bangla transliteration/pronunciation)
            if (userPrefs.showBanglaPronunciation && ayah.banglaPronunciation.isNotBlank()) {
                Text(
                    text = "বাংলা উচ্চারণ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ayah.banglaPronunciation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (userPrefs.banglaFontSize * 0.95f).sp,
                        lineHeight = (userPrefs.banglaFontSize * 1.4f).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 3. LAYER 3: English Meaning
            if (userPrefs.showEnglishMeaning && ayah.englishMeaning.isNotBlank()) {
                Text(
                    text = "English Meaning",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ayah.englishMeaning,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = userPrefs.englishFontSize.sp,
                        lineHeight = (userPrefs.englishFontSize * 1.4f).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 4. LAYER 4: বাংলা অর্থ (Bangla translation/meaning)
            if (userPrefs.showBanglaMeaning && ayah.banglaMeaning.isNotBlank()) {
                Text(
                    text = "বাংলা অর্থ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ayah.banglaMeaning,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = userPrefs.banglaFontSize.sp,
                        lineHeight = (userPrefs.banglaFontSize * 1.5f).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
