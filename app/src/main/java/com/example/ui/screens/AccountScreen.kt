package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.viewmodel.BotGeneratorViewModel

@Composable
fun AccountScreen(viewModel: BotGeneratorViewModel) {
    val project by viewModel.currentProject.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "AKUN DEVELOPER",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Profil pengembang & status lisensi BGR PRJCT",
                color = NeonCyan,
                fontSize = 12.sp
            )
        }

        // Profile Card
        item {
            GlassCard(
                borderColor = NeonViolet,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bgr_prjct_logo),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ARDZYN",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF22C55E).copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "VERIFIED",
                                    color = Color(0xFF4ADE80),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "@ardzyn • BGR PRJCT Creator",
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Role: Master Bot Architect & Lead Developer",
                            color = Color(0xFFA0AEC0),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Stats Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassCard(
                    borderColor = NeonCyan.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column {
                        Text(text = "Proyek Disimpan", color = Color.Gray, fontSize = 11.sp)
                        Text(
                            text = "${allProjects.size}",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = "Lokal Offline", color = NeonCyan, fontSize = 10.sp)
                    }
                }

                GlassCard(
                    borderColor = NeonViolet.copy(alpha = 0.4f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column {
                        Text(text = "Total Core", color = Color.Gray, fontSize = 11.sp)
                        Text(
                            text = "350",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = "17 Kategori", color = NeonViolet, fontSize = 10.sp)
                    }
                }
            }
        }

        // Badges Section
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "LENCANA KEHORMATAN",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    BadgeItem(
                        title = "BGR PRJCT Founder",
                        desc = "Pemrakarsa identitas graffiti bubble hitam putih dan galaksi.",
                        color = NeonCyan
                    )
                    BadgeItem(
                        title = "Singularity Master",
                        desc = "Pengembang sistem loading screen Black Hole interaktif.",
                        color = NeonViolet
                    )
                    BadgeItem(
                        title = "350 Modules Engineer",
                        desc = "Mengarsiteki 350 fitur modular bot multi-platform.",
                        color = NeonPink
                    )
                }
            }
        }

        // Credentials & Secret Placeholder
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "KONFIGURASI API & TOKEN LOKAL",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Aplikasi ini memprioritaskan privasi penuh secara offline. Seluruh token & kunci rahasia disimpan di perangkat Anda sendiri tanpa dikirim ke server luar.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun BadgeItem(title: String, desc: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "★", color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = Color(0xFFA0AEC0), fontSize = 11.sp)
        }
    }
}
