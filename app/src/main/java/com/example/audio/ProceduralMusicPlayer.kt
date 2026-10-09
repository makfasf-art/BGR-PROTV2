package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.data.model.MusicTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class ProceduralMusicPlayer {

    private val scope = CoroutineScope(Dispatchers.Default)
    private var playbackJob: Job? = null
    private var audioTrack: AudioTrack? = null

    val playlist: List<MusicTrack> = listOf(
        MusicTrack(
            id = 1,
            title = "Cosmic Drift (Lo-Fi Chill)",
            artist = "BGR Synth Lab",
            durationSeconds = 180,
            description = "Ambient lo-fi synthesizer chords dengan nada santai luar angkasa.",
            bpm = 75
        ),
        MusicTrack(
            id = 2,
            title = "Black Hole Horizon (Deep Space)",
            artist = "ARDZYN Audio Works",
            durationSeconds = 210,
            description = "Gelombang bass drone gravitasi dan resonance arpeggio misterius.",
            bpm = 60
        ),
        MusicTrack(
            id = 3,
            title = "BGR Neon Cyberwave",
            artist = "BGR PRJCT Beats",
            durationSeconds = 165,
            description = "Arpeggiator retro 80s bernuansa graffiti bubble neon.",
            bpm = 95
        ),
        MusicTrack(
            id = 4,
            title = "Nebula Echoes (Atmospheric)",
            artist = "ARDZYN Cosmic Sound",
            durationSeconds = 240,
            description = "Pad atmosferik lembut dengan gema kosmik yang menenangkan.",
            bpm = 70
        ),
        MusicTrack(
            id = 5,
            title = "Galactic Matrix Beat",
            artist = "BGR Sound System",
            durationSeconds = 195,
            description = "Ketukan ritmis futuristik untuk menemani konfigurasi bot.",
            bpm = 90
        )
    )

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0) // seconds
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _volume = MutableStateFlow(0.8f) // 0f to 1f
    val volume: StateFlow<Float> = _volume.asStateFlow()

    val currentTrack: MusicTrack
        get() = playlist[_currentTrackIndex.value.coerceIn(0, playlist.size - 1)]

    fun play() {
        if (_isPlaying.value) return
        _isPlaying.value = true
        startAudioGeneration()
    }

    fun pause() {
        _isPlaying.value = false
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) pause() else play()
    }

    fun next() {
        val nextIdx = (_currentTrackIndex.value + 1) % playlist.size
        selectTrack(nextIdx)
    }

    fun previous() {
        val prevIdx = if (_currentTrackIndex.value - 1 < 0) playlist.size - 1 else _currentTrackIndex.value - 1
        selectTrack(prevIdx)
    }

    fun selectTrack(index: Int) {
        val wasPlaying = _isPlaying.value
        pause()
        _currentTrackIndex.value = index.coerceIn(0, playlist.size - 1)
        _currentPosition.value = 0
        if (wasPlaying) {
            play()
        }
    }

    fun seekTo(seconds: Int) {
        val clamped = seconds.coerceIn(0, currentTrack.durationSeconds)
        _currentPosition.value = clamped
    }

    fun setVolume(vol: Float) {
        val clamped = vol.coerceIn(0f, 1f)
        _volume.value = clamped
        try {
            audioTrack?.setVolume(clamped)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startAudioGeneration() {
        playbackJob?.cancel()
        playbackJob = scope.launch {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.setVolume(_volume.value)
            audioTrack?.play()

            // Musical chords for each track
            val chordProgression = when (_currentTrackIndex.value) {
                0 -> listOf(220.0, 261.63, 329.63, 392.0) // A minor 7th lo-fi
                1 -> listOf(146.83, 174.61, 220.0, 261.63) // D minor deep space
                2 -> listOf(261.63, 329.63, 392.0, 523.25) // C major neon cyber
                3 -> listOf(196.0, 246.94, 293.66, 369.99) // G major 7th ethereal
                else -> listOf(220.0, 277.18, 329.63, 440.0) // A major synth
            }

            var noteStep = 0
            val noteDurationSamples = (sampleRate * 0.45).toInt()
            val shortBuffer = ShortArray(noteDurationSamples)

            while (isActive && _isPlaying.value) {
                val currentFreq = chordProgression[noteStep % chordProgression.size]
                val subBassFreq = currentFreq / 2.0

                for (i in 0 until noteDurationSamples) {
                    val t = i.toDouble() / sampleRate
                    // Synthesize soft sine wave + sub-bass + shimmer overtone with envelope
                    val envelope = sin((i.toDouble() / noteDurationSamples) * PI).coerceIn(0.0, 1.0)
                    val sample = (
                        sin(2.0 * PI * currentFreq * t) * 0.6 +
                        sin(2.0 * PI * subBassFreq * t) * 0.3 +
                        sin(2.0 * PI * (currentFreq * 2.0) * t) * 0.1
                    ) * envelope

                    val pcmValue = (sample * 16000.0).toInt().coerceIn(-32767, 32767).toShort()
                    shortBuffer[i] = pcmValue
                }

                audioTrack?.write(shortBuffer, 0, shortBuffer.size)

                noteStep++
                if (noteStep % 2 == 0) {
                    _currentPosition.value = (_currentPosition.value + 1)
                    if (_currentPosition.value >= currentTrack.durationSeconds) {
                        _currentPosition.value = 0
                        // loop or next track
                    }
                }
            }
        }
    }

    fun release() {
        pause()
        try {
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
    }
}
