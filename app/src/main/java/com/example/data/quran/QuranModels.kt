package com.example.data.quran

data class Ayah(
    val numberInSurah: Int,
    val arabic: String,
    val banglaPronunciation: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val audioUrl: String = ""
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameBangla: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val totalAyahs: Int,
    val revelationType: String, // Meccan or Medinan
    val startingJuz: Int,
    val ayahs: List<Ayah> = emptyList()
)

data class JuzInfo(
    val number: Int,
    val nameArabic: String,
    val nameBangla: String,
    val startSurahNumber: Int,
    val startAyahNumber: Int
)
