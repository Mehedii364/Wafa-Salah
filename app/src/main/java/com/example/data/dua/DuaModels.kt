package com.example.data.dua

data class DuaItem(
    val id: Int,
    val category: String, // Daily, Morning, Evening, Prayer, Travel, Food, Sleep, Parents, Study, Protection, Ramadan, Forgiveness, General
    val titleBn: String,
    val titleEn: String,
    val arabic: String,
    val banglaPronunciation: String,
    val englishMeaning: String,
    val banglaMeaning: String,
    val reference: String,
    val audioUrl: String = ""
)
