package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_records")
data class PrayerRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // YYYY-MM-DD
    val prayerName: String, // Fajr, Dhuhr, Asr, Maghrib, Isha
    val status: String, // COMPLETED, MISSED, PENDING
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quran_bookmarks")
data class QuranBookmark(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val ayahTextArabic: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "last_read")
data class LastReadPosition(
    @PrimaryKey val id: Int = 1,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "personal_duas")
data class PersonalDua(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val text: String,
    val notes: String = "",
    val category: String = "Personal", // Personal, Family, Study, Travel, Other
    val dateCreated: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_records")
data class TasbihRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zikirText: String,
    val count: Int,
    val target: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "ramadan_fasting")
data class RamadanFastRecord(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val isFasted: Boolean,
    val note: String = ""
)
