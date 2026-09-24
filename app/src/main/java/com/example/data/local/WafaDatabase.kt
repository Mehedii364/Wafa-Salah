package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PrayerRecord::class,
        QuranBookmark::class,
        LastReadPosition::class,
        PersonalDua::class,
        TasbihRecord::class,
        RamadanFastRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WafaDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    abstract fun quranDao(): QuranDao
    abstract fun personalDuaDao(): PersonalDuaDao
    abstract fun tasbihDao(): TasbihDao
    abstract fun ramadanDao(): RamadanDao

    companion object {
        @Volatile
        private var INSTANCE: WafaDatabase? = null

        fun getDatabase(context: Context): WafaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WafaDatabase::class.java,
                    "wafa_salah_tracker.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
