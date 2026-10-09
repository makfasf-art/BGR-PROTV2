package com.example.data.model

data class BotConfig(
    val botName: String = "BGR NEXUS BOT",
    val botUsername: String = "@bgr_nexus_bot",
    val prefix: String = "!",
    val ownerName: String = "ARDZYN",
    val ownerId: String = "6289512345678",
    val welcomeMessage: String = "Halo Selamat datang di server/grup! Patuhi peraturan dan selamat beraktivitas.",
    val goodbyeMessage: String = "Sampai jumpa kawan! Semoga sukses di perjalanan luar angkasamu.",
    val menuStyle: String = "Bubble Graffiti",
    val theme: String = "Galaxy Violet",
    val language: String = "Indonesian",
    val autoReply: Boolean = true,
    val antiSpam: Boolean = true,
    val maintenanceMode: Boolean = false
) {
    companion object {
        val MENU_STYLES = listOf(
            "Bubble Graffiti",
            "Modern Cyberpunk",
            "Classic Clean",
            "Minimalist Obsidian",
            "Galaxy Neon"
        )
        val THEMES = listOf(
            "Galaxy Violet",
            "Cosmic Cyan",
            "Neon Matrix",
            "Midnight Monochrome"
        )
        val LANGUAGES = listOf("Indonesian", "English")
    }
}
