package com.example.data.model

data class MusicTrack(
    val id: Int,
    val title: String,
    val artist: String,
    val durationSeconds: Int,
    val description: String,
    val bpm: Int = 85
)
