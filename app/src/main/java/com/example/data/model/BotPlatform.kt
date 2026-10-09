package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.TelegramBlue
import com.example.ui.theme.WhatsAppGreen

enum class BotPlatform(
    val displayName: String,
    val subtitle: String,
    val accentColor: Color,
    val defaultPrefix: String,
    val framework: String,
    val fileExtension: String,
    val description: String
) {
    WHATSAPP(
        displayName = "WhatsApp Bot",
        subtitle = "Baileys / Multi-Device Protocol",
        accentColor = WhatsAppGreen,
        defaultPrefix = ".",
        framework = "@whiskeysockets/baileys (Node.js)",
        fileExtension = "js",
        description = "Generator bot WhatsApp multi-device canggih berbasis Baileys library dengan dukungan QR pairing, buttons, interactive messages, dan media handler."
    ),
    DISCORD(
        displayName = "Discord Bot",
        subtitle = "Discord.js v14 / Gateway v10",
        accentColor = DiscordBlurple,
        defaultPrefix = "!",
        framework = "discord.js v14 (Node.js)",
        fileExtension = "js",
        description = "Generator bot Discord modern dengan slash commands, embeds, component buttons, auto-moderation, dan gateway event architecture."
    ),
    TELEGRAM(
        displayName = "Telegram Bot",
        subtitle = "Telegraf / Telegram Bot API",
        accentColor = TelegramBlue,
        defaultPrefix = "/",
        framework = "telegraf (Node.js)",
        fileExtension = "js",
        description = "Generator bot Telegram cepat dan tangguh menggunakan Telegraf framework dengan inline keyboards, webhook, dan media stream processing."
    )
}
