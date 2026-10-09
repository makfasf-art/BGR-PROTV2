package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BotPlatform
import com.example.generator.BotCodeGenerator
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.viewmodel.BotGeneratorViewModel

@Composable
fun GeneratorScreen(viewModel: BotGeneratorViewModel) {
    val project by viewModel.currentProject.collectAsState()
    val context = LocalContext.current

    val generatedFiles = remember(project) {
        BotCodeGenerator.generateProjectFiles(project)
    }

    var selectedFileIndex by remember { mutableIntStateOf(0) }
    val currentFile = generatedFiles.getOrElse(selectedFileIndex) { generatedFiles.first() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "PROJECT GENERATOR",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${project.platform.displayName} • ${project.config.botName}",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
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
                    text = project.platform.framework.take(18),
                    color = project.platform.accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Safety Notice Banner
        GlassCard(
            borderColor = Color(0xFFF59E0B).copy(alpha = 0.5f),
            backgroundColor = Color(0xCC1A1608),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Catatan Keamanan: Generator ini menghasilkan struktur source code & template konfigurasi lengkap. Bot tidak otomatis terhubung ke internet/server tanpa API token atau QR session yang Anda konfigurasikan.",
                    color = Color(0xFFFDE68A),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Platform Switcher Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotPlatform.values().forEach { platform ->
                val isSelected = project.platform == platform
                Button(
                    onClick = {
                        viewModel.selectPlatform(platform)
                        selectedFileIndex = 0
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) platform.accentColor else Color(0xFF191A2E)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = platform.name.take(4),
                        color = if (isSelected) Color.Black else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Generated Files Horizontal Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(generatedFiles) { idx, file ->
                val isSelected = idx == selectedFileIndex
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NeonViolet else Color(0xFF131526))
                        .border(1.dp, if (isSelected) NeonViolet else Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                        .clickable { selectedFileIndex = idx }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = file.fileName,
                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // File Code Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${currentFile.path} (${currentFile.language.uppercase()})",
                color = Color.Gray,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )

            Row {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(currentFile.fileName, currentFile.content)
                        clipboard.setPrimaryClip(clip)
                        viewModel.showSnackbar("${currentFile.fileName} disalin ke clipboard!")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Code",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, currentFile.content)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Bagikan ${currentFile.fileName}")
                        context.startActivity(shareIntent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share File",
                        tint = NeonViolet,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Code Viewer with Line Numbers
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF070810))
                .border(1.dp, Color(0xFF262842), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            val lines = currentFile.content.lines()
            val scrollStateHorizontal = rememberScrollState()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(lines) { lineIndex, lineText ->
                    Row(modifier = Modifier.horizontalScroll(scrollStateHorizontal)) {
                        Text(
                            text = String.format("%3d", lineIndex + 1),
                            color = Color(0xFF4A4D68),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.width(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = lineText,
                            color = when {
                                lineText.trim().startsWith("//") || lineText.trim().startsWith("#") -> Color(0xFF6B7280)
                                lineText.contains("import") || lineText.contains("export") || lineText.contains("const") || lineText.contains("async") -> NeonCyan
                                lineText.contains("function") || lineText.contains("return") -> NeonPink
                                lineText.contains("\"") || lineText.contains("'") -> Color(0xFFA7F3D0)
                                else -> Color(0xFFE2E8F0)
                            },
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Export All Files Action
        Button(
            onClick = {
                val fullBundle = buildString {
                    appendLine("// ================================================")
                    appendLine("// BOT GENERATOR PROJECT BUNDLE (BGR PRJCT)")
                    appendLine("// Platform: ${project.platform.displayName}")
                    appendLine("// Owner: ${project.config.ownerName}")
                    appendLine("// ================================================\n")
                    generatedFiles.forEach { f ->
                        appendLine("// ============ FILE: ${f.path} ============")
                        appendLine(f.content)
                        appendLine("\n")
                    }
                }
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("${project.name}_bundle", fullBundle)
                clipboard.setPrimaryClip(clip)
                viewModel.showSnackbar("Semua file project (${generatedFiles.size} file) berhasil disalin!")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Salin Seluruh Bundle Project (${generatedFiles.size} File)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
