package com.example

import android.app.Application
import com.example.data.local.WafaDatabase
import com.example.data.preferences.UserPreferencesRepository
import com.example.receiver.PrayerReminderReceiver

class WafaApplication : Application() {

    lateinit var database: WafaDatabase
        private set

    lateinit var preferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = WafaDatabase.getDatabase(this)
        preferencesRepository = UserPreferencesRepository(this)
        PrayerReminderReceiver.createPrayerNotificationChannel(this)
    }

    companion object {
        lateinit var instance: WafaApplication
            private set
    }
}
