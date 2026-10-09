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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.features.DefaultFeaturesCatalog
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.viewmodel.BotGeneratorViewModel

@Composable
fun SettingsScreen(viewModel: BotGeneratorViewModel) {
    val project by viewModel.currentProject.collectAsState()
    val volume by viewModel.musicPlayer.volume.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "PENGATURAN APLIKASI",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Preferensi audio, template, dan sistem cache",
                color = NeonCyan,
                fontSize = 12.sp
            )
        }

        // Audio Engine Settings
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "AUDIO & SYNTHESIZER",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Synthesizer audio offline bawaan menghasilkan gelombang ambient lo-fi secara prosedural tanpa internet dan bebas hak cipta.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Volume Musik Bawaan", color = Color(0xFFA0AEC0), fontSize = 12.sp)
                        Text(text = "${(volume * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Slider(
                        value = volume,
                        onValueChange = { viewModel.musicPlayer.setVolume(it) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = NeonCyan
                        )
                    )
                }
            }
        }

        // Cache & Reset Options
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "RESET & PEMELIHARAAN DATA",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            val resetFeatures = DefaultFeaturesCatalog.createInitial350Features()
                            val updated = project.copy(features = resetFeatures)
                            viewModel.createProject(project.name, project.platform)
                            viewModel.showSnackbar("Semua 350 fitur direset ke pengaturan default!")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33202E)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Reset 350 Fitur ke Katalog Awal", color = NeonPink, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.clearChat()
                            viewModel.showSnackbar("Cache simulasi chat & RAM dibersihkan!")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2038)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Bersihkan Cache Simulasi", color = NeonCyan, fontSize = 12.sp)
                    }
                }
            }
        }

        // Info System
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "INFORMASI SISTEM APLIKASI",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "App Name: BOT GENERATOR", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "Branding: BGR PRJCT", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "Owner / Creator: ARDZYN", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "Core Engine: Jetpack Compose + Local JSON Store", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "Versi: 1.0.0 Stable Release", color = Color.Gray, fontSize = 11.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
