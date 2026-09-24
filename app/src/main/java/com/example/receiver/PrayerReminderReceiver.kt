package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class PrayerReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID_PRAYER = "wafa_prayer_channel"
        const val EXTRA_PRAYER_NAME = "prayer_name"
        const val EXTRA_PRAYER_TIME = "prayer_time"
        const val EXTRA_IS_AZAN = "is_azan"

        fun triggerNotification(
            context: Context,
            prayerName: String,
            prayerTime: String,
            isAzan: Boolean = true,
            vibrate: Boolean = true
        ) {
            createPrayerNotificationChannel(context)

            val openIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                prayerName.hashCode(),
                openIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val prayerNameBn = when (prayerName) {
                "Fajr" -> "ফজর"
                "Sunrise" -> "সূর্যোদয়"
                "Dhuhr" -> "যোহর"
                "Asr" -> "আসর"
                "Maghrib" -> "মাগরিব"
                "Isha" -> "ইশা"
                else -> prayerName
            }

            val title = "ওয়াক্ত হয়েছে: $prayerNameBn ($prayerName)"
            val message = "নামাজের সময় হয়েছে ($prayerTime)। সালাত আদায় করতে প্রস্তুত হোন।"

            val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID_PRAYER)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(defaultSoundUri)

            if (vibrate) {
                builder.setVibrate(longArrayOf(0, 500, 200, 500))
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(prayerName.hashCode(), builder.build())

            if (vibrate) {
                try {
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    vibrator?.vibrate(500)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun createPrayerNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID_PRAYER,
                    "Wafa Prayer Time Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Timely Azan and Salah reminders"
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 200, 500)
                    val attributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)
                }
                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "Prayer"
        val prayerTime = intent.getStringExtra(EXTRA_PRAYER_TIME) ?: ""
        val isAzan = intent.getBooleanExtra(EXTRA_IS_AZAN, true)
        triggerNotification(context, prayerName, prayerTime, isAzan)
    }
}
