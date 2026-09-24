package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wafa_settings")

data class UserPreferences(
    val language: String = "bn",
    val themeMode: String = "system", // system, light, dark
    val calculationMethod: String = "KARACHI",
    val asrMethod: String = "HANAFI",
    val cityName: String = "Dhaka, Bangladesh",
    val latitude: Double = 23.8103,
    val longitude: Double = 90.4125,
    val is24Hour: Boolean = false,
    val notifyFajr: Boolean = true,
    val notifyDhuhr: Boolean = true,
    val notifyAsr: Boolean = true,
    val notifyMaghrib: Boolean = true,
    val notifyIsha: Boolean = true,
    val azanSoundEnabled: Boolean = true,
    val reminderMinutesBefore: Int = 0,
    val vibrateEnabled: Boolean = true,
    val showArabic: Boolean = true,
    val showBanglaPronunciation: Boolean = true,
    val showEnglishMeaning: Boolean = true,
    val showBanglaMeaning: Boolean = true,
    val arabicFontSize: Float = 26f,
    val banglaFontSize: Float = 16f,
    val englishFontSize: Float = 15f,
    val floatingWidgetEnabled: Boolean = false,
    val showDailyAyah: Boolean = true,
    val showDailyDua: Boolean = true,
    val showDailyHadith: Boolean = true,
    val showQuickTasbih: Boolean = true
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CALCULATION_METHOD = stringPreferencesKey("calculation_method")
        val ASR_METHOD = stringPreferencesKey("asr_method")
        val CITY_NAME = stringPreferencesKey("city_name")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
        val IS_24_HOUR = booleanPreferencesKey("is_24_hour")
        val NOTIFY_FAJR = booleanPreferencesKey("notify_fajr")
        val NOTIFY_DHUHR = booleanPreferencesKey("notify_dhuhr")
        val NOTIFY_ASR = booleanPreferencesKey("notify_asr")
        val NOTIFY_MAGHRIB = booleanPreferencesKey("notify_maghrib")
        val NOTIFY_ISHA = booleanPreferencesKey("notify_isha")
        val AZAN_SOUND = booleanPreferencesKey("azan_sound")
        val REMINDER_MINUTES = intPreferencesKey("reminder_minutes")
        val VIBRATE = booleanPreferencesKey("vibrate")
        val SHOW_ARABIC = booleanPreferencesKey("show_arabic")
        val SHOW_BANGLA_PRONUNCIATION = booleanPreferencesKey("show_bangla_pronunciation")
        val SHOW_ENGLISH_MEANING = booleanPreferencesKey("show_english_meaning")
        val SHOW_BANGLA_MEANING = booleanPreferencesKey("show_bangla_meaning")
        val ARABIC_FONT_SIZE = floatPreferencesKey("arabic_font_size")
        val BANGLA_FONT_SIZE = floatPreferencesKey("bangla_font_size")
        val ENGLISH_FONT_SIZE = floatPreferencesKey("english_font_size")
        val FLOATING_WIDGET = booleanPreferencesKey("floating_widget")
        val SHOW_DAILY_AYAH = booleanPreferencesKey("show_daily_ayah")
        val SHOW_DAILY_DUA = booleanPreferencesKey("show_daily_dua")
        val SHOW_DAILY_HADITH = booleanPreferencesKey("show_daily_hadith")
        val SHOW_QUICK_TASBIH = booleanPreferencesKey("show_quick_tasbih")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { pref ->
        UserPreferences(
            language = pref[PreferencesKeys.LANGUAGE] ?: "bn",
            themeMode = pref[PreferencesKeys.THEME_MODE] ?: "system",
            calculationMethod = pref[PreferencesKeys.CALCULATION_METHOD] ?: "KARACHI",
            asrMethod = pref[PreferencesKeys.ASR_METHOD] ?: "HANAFI",
            cityName = pref[PreferencesKeys.CITY_NAME] ?: "Dhaka, Bangladesh",
            latitude = pref[PreferencesKeys.LATITUDE] ?: 23.8103,
            longitude = pref[PreferencesKeys.LONGITUDE] ?: 90.4125,
            is24Hour = pref[PreferencesKeys.IS_24_HOUR] ?: false,
            notifyFajr = pref[PreferencesKeys.NOTIFY_FAJR] ?: true,
            notifyDhuhr = pref[PreferencesKeys.NOTIFY_DHUHR] ?: true,
            notifyAsr = pref[PreferencesKeys.NOTIFY_ASR] ?: true,
            notifyMaghrib = pref[PreferencesKeys.NOTIFY_MAGHRIB] ?: true,
            notifyIsha = pref[PreferencesKeys.NOTIFY_ISHA] ?: true,
            azanSoundEnabled = pref[PreferencesKeys.AZAN_SOUND] ?: true,
            reminderMinutesBefore = pref[PreferencesKeys.REMINDER_MINUTES] ?: 0,
            vibrateEnabled = pref[PreferencesKeys.VIBRATE] ?: true,
            showArabic = pref[PreferencesKeys.SHOW_ARABIC] ?: true,
            showBanglaPronunciation = pref[PreferencesKeys.SHOW_BANGLA_PRONUNCIATION] ?: true,
            showEnglishMeaning = pref[PreferencesKeys.SHOW_ENGLISH_MEANING] ?: true,
            showBanglaMeaning = pref[PreferencesKeys.SHOW_BANGLA_MEANING] ?: true,
            arabicFontSize = pref[PreferencesKeys.ARABIC_FONT_SIZE] ?: 26f,
            banglaFontSize = pref[PreferencesKeys.BANGLA_FONT_SIZE] ?: 16f,
            englishFontSize = pref[PreferencesKeys.ENGLISH_FONT_SIZE] ?: 15f,
            floatingWidgetEnabled = pref[PreferencesKeys.FLOATING_WIDGET] ?: false,
            showDailyAyah = pref[PreferencesKeys.SHOW_DAILY_AYAH] ?: true,
            showDailyDua = pref[PreferencesKeys.SHOW_DAILY_DUA] ?: true,
            showDailyHadith = pref[PreferencesKeys.SHOW_DAILY_HADITH] ?: true,
            showQuickTasbih = pref[PreferencesKeys.SHOW_QUICK_TASBIH] ?: true
        )
    }

    suspend fun updateLanguage(lang: String) = context.dataStore.edit { it[PreferencesKeys.LANGUAGE] = lang }
    suspend fun updateTheme(mode: String) = context.dataStore.edit { it[PreferencesKeys.THEME_MODE] = mode }
    suspend fun updateLocation(city: String, lat: Double, lng: Double) = context.dataStore.edit {
        it[PreferencesKeys.CITY_NAME] = city
        it[PreferencesKeys.LATITUDE] = lat
        it[PreferencesKeys.LONGITUDE] = lng
    }
    suspend fun updateCalculationMethod(method: String) = context.dataStore.edit { it[PreferencesKeys.CALCULATION_METHOD] = method }
    suspend fun updateAsrMethod(method: String) = context.dataStore.edit { it[PreferencesKeys.ASR_METHOD] = method }
    suspend fun updateIs24Hour(is24: Boolean) = context.dataStore.edit { it[PreferencesKeys.IS_24_HOUR] = is24 }
    suspend fun updatePrayerNotification(prayer: String, enabled: Boolean) = context.dataStore.edit {
        when (prayer) {
            "Fajr" -> it[PreferencesKeys.NOTIFY_FAJR] = enabled
            "Dhuhr" -> it[PreferencesKeys.NOTIFY_DHUHR] = enabled
            "Asr" -> it[PreferencesKeys.NOTIFY_ASR] = enabled
            "Maghrib" -> it[PreferencesKeys.NOTIFY_MAGHRIB] = enabled
            "Isha" -> it[PreferencesKeys.NOTIFY_ISHA] = enabled
        }
    }
    suspend fun updateAzanSound(enabled: Boolean) = context.dataStore.edit { it[PreferencesKeys.AZAN_SOUND] = enabled }
    suspend fun updateReminderMinutes(minutes: Int) = context.dataStore.edit { it[PreferencesKeys.REMINDER_MINUTES] = minutes }
    suspend fun updateVibrate(enabled: Boolean) = context.dataStore.edit { it[PreferencesKeys.VIBRATE] = enabled }

    suspend fun updateQuranLayers(arabic: Boolean, banglaPron: Boolean, engMean: Boolean, bngMean: Boolean) = context.dataStore.edit {
        it[PreferencesKeys.SHOW_ARABIC] = arabic
        it[PreferencesKeys.SHOW_BANGLA_PRONUNCIATION] = banglaPron
        it[PreferencesKeys.SHOW_ENGLISH_MEANING] = engMean
        it[PreferencesKeys.SHOW_BANGLA_MEANING] = bngMean
    }
    suspend fun updateQuranFontSizes(arabic: Float, bangla: Float, english: Float) = context.dataStore.edit {
        it[PreferencesKeys.ARABIC_FONT_SIZE] = arabic
        it[PreferencesKeys.BANGLA_FONT_SIZE] = bangla
        it[PreferencesKeys.ENGLISH_FONT_SIZE] = english
    }
    suspend fun updateFloatingWidget(enabled: Boolean) = context.dataStore.edit { it[PreferencesKeys.FLOATING_WIDGET] = enabled }
    suspend fun updateHomeSection(section: String, visible: Boolean) = context.dataStore.edit {
        when (section) {
            "ayah" -> it[PreferencesKeys.SHOW_DAILY_AYAH] = visible
            "dua" -> it[PreferencesKeys.SHOW_DAILY_DUA] = visible
            "hadith" -> it[PreferencesKeys.SHOW_DAILY_HADITH] = visible
            "tasbih" -> it[PreferencesKeys.SHOW_QUICK_TASBIH] = visible
        }
    }
}
