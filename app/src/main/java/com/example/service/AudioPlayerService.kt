package com.example.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.*

class AudioPlayerService : Service() {

    companion object {
        const val CHANNEL_ID = "wafa_audio_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_PLAY = "com.example.wafa.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.wafa.ACTION_PAUSE"
        const val ACTION_RESUME = "com.example.wafa.ACTION_RESUME"
        const val ACTION_STOP = "com.example.wafa.ACTION_STOP"
        const val ACTION_SEEK = "com.example.wafa.ACTION_SEEK"
        const val ACTION_SET_SPEED = "com.example.wafa.ACTION_SET_SPEED"

        const val EXTRA_URL = "extra_url"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SUBTITLE = "extra_subtitle"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_SPEED = "extra_speed"
    }

    private var mediaPlayer: MediaPlayer? = null
    private var serviceJob = SupervisorJob()
    private var serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var progressJob: Job? = null

    private var currentTitle = "Wafa Audio"
    private var currentSubtitle = "Quran Recitation"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val url = intent.getStringExtra(EXTRA_URL) ?: ""
                currentTitle = intent.getStringExtra(EXTRA_TITLE) ?: "Wafa Audio"
                currentSubtitle = intent.getStringExtra(EXTRA_SUBTITLE) ?: "Quran Recitation"
                playAudio(url)
            }
            ACTION_PAUSE -> pauseAudio()
            ACTION_RESUME -> resumeAudio()
            ACTION_STOP -> stopAudio()
            ACTION_SEEK -> {
                val pos = intent.getLongExtra(EXTRA_POSITION, 0L)
                mediaPlayer?.seekTo(pos.toInt())
            }
            ACTION_SET_SPEED -> {
                val speed = intent.getFloatExtra(EXTRA_SPEED, 1.0f)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed)
                            ?: android.media.PlaybackParams().setSpeed(speed)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun playAudio(url: String) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp ->
                    mp.start()
                    startForeground(NOTIFICATION_ID, buildNotification(true))
                    AudioPlayerManager.updateInternalState(true, 0, mp.duration.toLong())
                    startProgressTracker()
                }
                setOnCompletionListener {
                    AudioPlayerManager.updateInternalState(false, 0, 0)
                    stopAudio()
                }
                setOnErrorListener { _, _, _ ->
                    AudioPlayerManager.updateInternalState(false, 0, 0)
                    stopAudio()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopAudio()
        }
    }

    private fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                AudioPlayerManager.updateInternalState(false, it.currentPosition.toLong(), it.duration.toLong())
                updateNotification(false)
            }
        }
    }

    private fun resumeAudio() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                AudioPlayerManager.updateInternalState(true, it.currentPosition.toLong(), it.duration.toLong())
                updateNotification(true)
            }
        }
    }

    private fun stopAudio() {
        progressJob?.cancel()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (isActive) {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        AudioPlayerManager.updateInternalState(true, mp.currentPosition.toLong(), mp.duration.toLong())
                    }
                }
                delay(1000)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Wafa Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Audio playback controls for Quran, Dua and Islamic recitation"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val openAppIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val toggleActionIntent = Intent(this, AudioPlayerService::class.java).apply {
            action = if (isPlaying) ACTION_PAUSE else ACTION_RESUME
        }
        val togglePendingIntent = PendingIntent.getService(
            this,
            1,
            toggleActionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val stopIntent = Intent(this, AudioPlayerService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            2,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTitle)
            .setContentText(currentSubtitle)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(openAppIntent)
            .setOngoing(isPlaying)
            .addAction(
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (isPlaying) "Pause" else "Play",
                togglePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop",
                stopPendingIntent
            )
            .build()
    }

    private fun updateNotification(isPlaying: Boolean) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification(isPlaying))
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
