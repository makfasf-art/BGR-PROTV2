package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BotPlatform
import com.example.ui.components.BgrGraffitiLogoBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SpaceDark
import com.example.viewmodel.BotGeneratorViewModel
import com.example.viewmodel.MainTab

@Composable
fun HomeScreen(
    viewModel: BotGeneratorViewModel,
    onOpenMusicPlayer: () -> Unit
) {
    val project by viewModel.currentProject.collectAsState()
    val totalFeatures = project.features.size
    val activeFeatures = project.features.count { it.isEnabled }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BgrGraffitiLogoBadge(size = 46.dp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1B1B30))
                        .border(1.dp, NeonViolet.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "DEV: ARDZYN",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Hero Banner
        item {
            GlassCard(
                borderColor = NeonViolet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "BOT GENERATOR",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Galaxy Edition • 350 Customizable Cores",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(project.platform.accentColor.copy(alpha = 0.2f))
                                .border(1.dp, project.platform.accentColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = project.platform.displayName,
                                color = project.platform.accentColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Konfigurasikan arsitektur bot WhatsApp, Discord, dan Telegram dengan mudah, pilih dari 350 modul perintah, dan ekspor source code siap pakai.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.setTab(MainTab.FEATURES) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Kelola 350 Fitur", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.setTab(MainTab.GENERATOR) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E38)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Buka Generator", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Stats Dashboard
        item {
            Text(
                text = "DASHBOARD STATISTIK",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total Fitur",
                    value = "$totalFeatures",
                    subtitle = "Tersedia",
                    accentColor = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Fitur Aktif",
                    value = "$activeFeatures",
                    subtitle = "Siap Ekspor",
                    accentColor = NeonViolet,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Platform",
                    value = project.platform.name.take(4),
                    subtitle = project.config.prefix,
                    accentColor = project.platform.accentColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Platform Switcher Section
        item {
            Text(
                text = "PILIH PLATFORM BOT",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BotPlatform.values().forEach { platform ->
                    val isSelected = project.platform == platform
                    GlassCard(
                        borderColor = if (isSelected) platform.accentColor else NeonViolet.copy(alpha = 0.2f),
                        backgroundColor = if (isSelected) SpaceDark.copy(alpha = 0.95f) else Color(0x990C0E1E),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectPlatform(platform) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) platform.accentColor else Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = platform.displayName,
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = platform.subtitle,
                                        color = Color(0xFFA0AEC0),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) platform.accentColor.copy(alpha = 0.2f) else Color.Transparent)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isSelected) "AKTIF" else "PILIH",
                                    color = if (isSelected) platform.accentColor else Color.Gray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bot Configuration Summary Card
        item {
            Text(
                text = "KONFIGURASI BOT AKTIF",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ConfigRow(label = "Nama Bot", value = project.config.botName)
                    ConfigRow(label = "Username", value = project.config.botUsername)
                    ConfigRow(label = "Default Prefix", value = project.config.prefix)
                    ConfigRow(label = "Owner Creator", value = project.config.ownerName)
                    ConfigRow(label = "Menu Style", value = project.config.menuStyle)
                    ConfigRow(label = "Theme", value = project.config.theme)
                    ConfigRow(label = "Auto Reply", value = if (project.config.autoReply) "Aktif (ON)" else "Nonaktif")
                    ConfigRow(label = "Maintenance", value = if (project.config.maintenanceMode) "Mode Perawatan" else "Normal")

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { viewModel.setTab(MainTab.SETTINGS) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252644)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Edit Seluruh Konfigurasi", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Quick Shortcuts Grid
        item {
            Text(
                text = "AKSES CEPAT FITUR",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = "Simulator Chat",
                    icon = Icons.Default.Chat,
                    color = NeonCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setTab(MainTab.PREVIEW) }
                )
                QuickActionItem(
                    title = "Project Manager",
                    icon = Icons.Default.Folder,
                    color = NeonViolet,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setTab(MainTab.PROJECTS) }
                )
                QuickActionItem(
                    title = "Music Player",
                    icon = Icons.Default.MusicNote,
                    color = NeonPink,
                    modifier = Modifier.weight(1f),
                    onClick = onOpenMusicPlayer
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp)) // padding for bottom bar
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        borderColor = accentColor.copy(alpha = 0.4f),
        modifier = modifier
    ) {
        Column {
            Text(text = title, color = Color.Gray, fontSize = 11.sp)
            Text(
                text = value,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(text = subtitle, color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ConfigRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFFA0AEC0), fontSize = 12.sp)
        Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        borderColor = color.copy(alpha = 0.3f),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
