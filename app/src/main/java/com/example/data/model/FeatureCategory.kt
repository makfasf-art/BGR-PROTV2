package com.example.data.model

enum class FeatureCategory(val displayName: String, val iconName: String) {
    GENERAL("General", "apps"),
    UTILITY("Utility", "build"),
    ADMIN("Admin", "shield"),
    GROUP("Group", "group"),
    MODERATION("Moderation", "security"),
    GAMES("Games", "sports_esports"),
    AI("AI", "psychology"),
    MEDIA("Media", "perm_media"),
    DOWNLOADER("Downloader", "download"),
    STICKER("Sticker", "sticky_note_2"),
    FUN("Fun", "sentiment_very_satisfied"),
    TOOLS("Tools", "handyman"),
    OWNER("Owner", "vpn_key"),
    AUTOMATION("Automation", "smart_toy"),
    INFORMATION("Information", "info"),
    SECURITY("Security", "lock"),
    CUSTOM("Custom", "extension")
}

enum class FeaturePermission(val displayName: String) {
    PUBLIC("Public"),
    ADMIN("Admin"),
    PREMIUM("Premium"),
    OWNER("Owner")
}
