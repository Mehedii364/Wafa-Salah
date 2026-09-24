package com.example.service

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AudioTrack(
    val title: String,
    val subtitle: String,
    val url: String,
    val surahNumber: Int = 0,
    val ayahNumber: Int = 0
)

object AudioPlayerManager {

    private val _currentTrack = MutableStateFlow<AudioTrack?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs = _durationMs.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed = _playbackSpeed.asStateFlow()

    private val _sleepTimerMinutes = MutableStateFlow(0)
    val sleepTimerMinutes = _sleepTimerMinutes.asStateFlow()

    private var sleepJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun play(context: Context, track: AudioTrack) {
        _currentTrack.value = track
        _isPlaying.value = true
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_PLAY
            putExtra(AudioPlayerService.EXTRA_URL, track.url)
            putExtra(AudioPlayerService.EXTRA_TITLE, track.title)
            putExtra(AudioPlayerService.EXTRA_SUBTITLE, track.subtitle)
        }
        context.startService(intent)
    }

    fun pause(context: Context) {
        _isPlaying.value = false
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_PAUSE
        }
        context.startService(intent)
    }

    fun resume(context: Context) {
        _isPlaying.value = true
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_RESUME
        }
        context.startService(intent)
    }

    fun stop(context: Context) {
        _isPlaying.value = false
        _currentTrack.value = null
        _currentPositionMs.value = 0L
        _durationMs.value = 0L
        sleepJob?.cancel()
        _sleepTimerMinutes.value = 0
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_STOP
        }
        context.startService(intent)
    }

    fun seekTo(context: Context, positionMs: Long) {
        _currentPositionMs.value = positionMs
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_SEEK
            putExtra(AudioPlayerService.EXTRA_POSITION, positionMs)
        }
        context.startService(intent)
    }

    fun setSpeed(context: Context, speed: Float) {
        _playbackSpeed.value = speed
        val intent = Intent(context, AudioPlayerService::class.java).apply {
            action = AudioPlayerService.ACTION_SET_SPEED
            putExtra(AudioPlayerService.EXTRA_SPEED, speed)
        }
        context.startService(intent)
    }

    fun setSleepTimer(context: Context, minutes: Int) {
        _sleepTimerMinutes.value = minutes
        sleepJob?.cancel()
        if (minutes > 0) {
            sleepJob = scope.launch {
                delay(minutes * 60 * 1000L)
                stop(context)
            }
        }
    }

    // Called by AudioPlayerService
    fun updateInternalState(playing: Boolean, position: Long, duration: Long) {
        _isPlaying.value = playing
        _currentPositionMs.value = position
        if (duration > 0) {
            _durationMs.value = duration
        }
    }
}
