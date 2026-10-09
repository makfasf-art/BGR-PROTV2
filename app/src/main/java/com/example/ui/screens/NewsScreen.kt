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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet

private data class NewsArticle(
    val title: String,
    val date: String,
    val category: String,
    val summary: String
)

@Composable
fun NewsScreen() {
    val newsList = listOf(
        NewsArticle(
            title = "Rilis Resmi BOT GENERATOR v1.0 Galaxy Edition",
            date = "08 Okt 2026",
            category = "RELEASE",
            summary = "BGR PRJCT resmi meluncurkan BOT GENERATOR dengan arsitektur 350 fitur siap kustomisasi, visualisasi Black Hole interaktif, dan pemutar musik synthesizer kosmik."
        ),
        NewsArticle(
            title = "Pembaruan Baileys v6.7 Multi-Device Protocol",
            date = "05 Okt 2026",
            category = "PROTOCOL",
            summary = "Protokol WhatsApp Baileys kini mendukung pairing code otomatis tanpa harus menggunakan kamera fisik untuk scan QR code di terminal."
        ),
        NewsArticle(
            title = "Tips Mengamankan Bot Discord dari Serangan Raid",
            date = "01 Okt 2026",
            category = "SECURITY",
            summary = "Gunakan kombinasi fitur Anti Mass Mention dan Captcha Verifikasi pada kategori Moderasi untuk mencegah bot asing membanjiri server Anda."
        ),
        NewsArticle(
            title = "Integrasi Model AI Terbaru untuk Respon Bot Cerdas",
            date = "28 Sep 2026",
            category = "AI ENGINE",
            summary = "Modul AI pada BOT GENERATOR kini mencakup dukungan prompt Gemini, Claude Logic, dan Text-to-Speech untuk pengalaman interaksi obrolan yang lebih hidup."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "BERITA & CATATAN RILIS",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Kabar terkini seputar ekosistem bot dan BGR PRJCT",
                color = NeonCyan,
                fontSize = 12.sp
            )
        }

        items(newsList.size) { idx ->
            val article = newsList[idx]
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = article.category,
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = article.date,
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = article.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = article.summary,
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
