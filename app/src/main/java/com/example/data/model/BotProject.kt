package com.example.data.model

import java.util.UUID

data class BotProject(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Project Bot Utama",
    val platform: BotPlatform = BotPlatform.WHATSAPP,
    val config: BotConfig = BotConfig(),
    val features: List<BotFeature> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
