package com.example.data.model

data class BotFeature(
    val id: Int,
    val name: String,
    val command: String,
    val prefix: String = "",
    val description: String,
    val category: FeatureCategory,
    val permission: FeaturePermission = FeaturePermission.PUBLIC,
    val response: String,
    val isEnabled: Boolean = true
)
