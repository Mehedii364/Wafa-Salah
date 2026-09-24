package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {
    @Query("SELECT * FROM prayer_records WHERE date = :date")
    fun getRecordsForDate(date: String): Flow<List<PrayerRecord>>

    @Query("SELECT * FROM prayer_records WHERE date BETWEEN :startDate AND :endDate")
    fun getRecordsBetweenDates(startDate: String, endDate: String): Flow<List<PrayerRecord>>

    @Query("SELECT * FROM prayer_records ORDER BY date DESC, completedAt DESC LIMIT 200")
    fun getAllHistory(): Flow<List<PrayerRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: PrayerRecord)

    @Query("DELETE FROM prayer_records WHERE date = :date AND prayerName = :prayerName")
    suspend fun deleteRecord(date: String, prayerName: String)

    @Query("SELECT COUNT(*) FROM prayer_records WHERE status = 'COMPLETED'")
    fun getTotalCompletedCount(): Flow<Int>
}

@Dao
interface QuranDao {
    @Query("SELECT * FROM quran_bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<QuranBookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: QuranBookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: QuranBookmark)

    @Query("DELETE FROM quran_bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber")
    suspend fun removeBookmark(surahNumber: Int, ayahNumber: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM quran_bookmarks WHERE surahNumber = :surahNumber AND ayahNumber = :ayahNumber)")
    fun isBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean>

    @Query("SELECT * FROM last_read WHERE id = 1 LIMIT 1")
    fun getLastRead(): Flow<LastReadPosition?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastRead(position: LastReadPosition)
}

@Dao
interface PersonalDuaDao {
    @Query("SELECT * FROM personal_duas ORDER BY dateCreated DESC")
    fun getAllDuas(): Flow<List<PersonalDua>>

    @Query("SELECT * FROM personal_duas WHERE category = :category ORDER BY dateCreated DESC")
    fun getDuasByCategory(category: String): Flow<List<PersonalDua>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDua(dua: PersonalDua)

    @Update
    suspend fun updateDua(dua: PersonalDua)

    @Delete
    suspend fun deleteDua(dua: PersonalDua)
}

@Dao
interface TasbihDao {
    @Query("SELECT * FROM tasbih_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<TasbihRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: TasbihRecord)

    @Query("DELETE FROM tasbih_records WHERE id = :id")
    suspend fun deleteRecord(id: Long)
}

@Dao
interface RamadanDao {
    @Query("SELECT * FROM ramadan_fasting ORDER BY date ASC")
    fun getAllFastRecords(): Flow<List<RamadanFastRecord>>

    @Query("SELECT * FROM ramadan_fasting WHERE date = :date LIMIT 1")
    fun getRecordForDate(date: String): Flow<RamadanFastRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecord(record: RamadanFastRecord)
}
