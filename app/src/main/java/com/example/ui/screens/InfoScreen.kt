package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet

@Composable
fun InfoScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "PANDUAN & INFORMASI",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Cara menjalankan bot hasil generator di server atau VPS",
                color = NeonCyan,
                fontSize = 12.sp
            )
        }

        // WhatsApp Setup Guide
        item {
            GlassCard(
                borderColor = Color(0xFF25D366).copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "1. PANDUAN RUNNING WHATSAPP BOT",
                        color = Color(0xFF4ADE80),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Download file project hasil generator ke VPS atau komputer lokal.\n• Ekstrak file dan jalankan perintah:\n  npm install\n  node index.js\n• Buka aplikasi WhatsApp di HP Anda -> Pengaturan -> Perangkat Tertaut (Linked Devices) -> Tautkan Perangkat.\n• Scan QR Code yang tercetak di layar terminal.\n• Bot akan langsung online dan siap menerima perintah dengan prefix Anda.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Discord Setup Guide
        item {
            GlassCard(
                borderColor = Color(0xFF5865F2).copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "2. PANDUAN RUNNING DISCORD BOT",
                        color = Color(0xFF818CF8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Kunjungi Discord Developer Portal (discord.com/developers).\n• Klik 'New Application' dan beri nama bot Anda.\n• Di menu 'Bot', klik 'Reset Token' untuk mendapatkan bot token rahasia.\n• WAJIB AKTIFKAN 'Privileged Gateway Intents':\n  - Presence Intent\n  - Server Members Intent\n  - Message Content Intent\n• Tempel token Anda di file .env atau config.json.\n• Jalankan perintah: npm install && node index.js.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Telegram Setup Guide
        item {
            GlassCard(
                borderColor = Color(0xFF229ED9).copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "3. PANDUAN RUNNING TELEGRAM BOT",
                        color = Color(0xFF67E8F9),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Buka aplikasi Telegram dan cari @BotFather.\n• Kirim perintah /newbot dan ikuti instruksi penamaan bot Anda.\n• Salin HTTP API Token yang diberikan oleh BotFather.\n• Masukkan token ke file .env:\n  BOT_TOKEN=123456789:ABCDEF...\n• Jalankan: npm install && node bot.js.\n• Bot Anda siap digunakan di obrolan pribadi maupun grup Telegram.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Hosting Deployment Recommendations
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "4. REKOMENDASI HOSTING CLOUD & VPS",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• VPS Ubuntu 22.04 LTS (DigitalOcean, Linode, AWS EC2, IDCloudHost)\n• Manajemen Proses: Gunakan PM2 (pm2 start index.js --name bgr-bot)\n• Platform Gratis / PaaS: Railway.app, Render.com, Fly.io\n• Docker Container: Sangat disarankan untuk mempermudah backup dan isolasi dependensi.",
                        color = Color(0xFFA0AEC0),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
