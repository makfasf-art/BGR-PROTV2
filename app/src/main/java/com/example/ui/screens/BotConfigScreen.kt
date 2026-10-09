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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BotConfig
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SpaceDark
import com.example.viewmodel.BotGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotConfigScreen(viewModel: BotGeneratorViewModel) {
    val project by viewModel.currentProject.collectAsState()
    val initialCfg = project.config

    var botName by remember(initialCfg) { mutableStateOf(initialCfg.botName) }
    var botUsername by remember(initialCfg) { mutableStateOf(initialCfg.botUsername) }
    var prefix by remember(initialCfg) { mutableStateOf(initialCfg.prefix) }
    var ownerName by remember(initialCfg) { mutableStateOf(initialCfg.ownerName) }
    var ownerId by remember(initialCfg) { mutableStateOf(initialCfg.ownerId) }
    var welcomeMessage by remember(initialCfg) { mutableStateOf(initialCfg.welcomeMessage) }
    var goodbyeMessage by remember(initialCfg) { mutableStateOf(initialCfg.goodbyeMessage) }
    var menuStyle by remember(initialCfg) { mutableStateOf(initialCfg.menuStyle) }
    var theme by remember(initialCfg) { mutableStateOf(initialCfg.theme) }
    var language by remember(initialCfg) { mutableStateOf(initialCfg.language) }
    var autoReply by remember(initialCfg) { mutableStateOf(initialCfg.autoReply) }
    var antiSpam by remember(initialCfg) { mutableStateOf(initialCfg.antiSpam) }
    var maintenanceMode by remember(initialCfg) { mutableStateOf(initialCfg.maintenanceMode) }

    var expandedMenuStyle by remember { mutableStateOf(false) }
    var expandedTheme by remember { mutableStateOf(false) }
    var expandedLang by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "BOT CONFIGURATION",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Sesuaikan identitas, perilaku, dan preferensi bot Anda",
                color = NeonCyan,
                fontSize = 12.sp
            )
        }

        // Section 1: Identitas Bot
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "1. IDENTITAS BOT",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ConfigTextField(
                        label = "Bot Name",
                        value = botName,
                        onValueChange = { botName = it }
                    )

                    ConfigTextField(
                        label = "Bot Username",
                        value = botUsername,
                        onValueChange = { botUsername = it }
                    )

                    ConfigTextField(
                        label = "Prefix Perintah (contoh: !, ., /)",
                        value = prefix,
                        onValueChange = { prefix = it }
                    )
                }
            }
        }

        // Section 2: Owner & Creator
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "2. OWNER & CREATOR",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ConfigTextField(
                        label = "Owner Name",
                        value = ownerName,
                        onValueChange = { ownerName = it }
                    )

                    ConfigTextField(
                        label = "Owner ID / Nomor Telepon",
                        value = ownerId,
                        onValueChange = { ownerId = it }
                    )
                }
            }
        }

        // Section 3: Pesan Sambutan & Perpisahan
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "3. PESAN GRUP & NOTIFIKASI",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ConfigTextField(
                        label = "Welcome Message (Member Baru)",
                        value = welcomeMessage,
                        onValueChange = { welcomeMessage = it },
                        singleLine = false
                    )

                    ConfigTextField(
                        label = "Goodbye Message (Member Keluar)",
                        value = goodbyeMessage,
                        onValueChange = { goodbyeMessage = it },
                        singleLine = false
                    )
                }
            }
        }

        // Section 4: Gaya & Tampilan
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "4. GAYA & TAMPILAN MENU",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Menu Style Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedMenuStyle,
                        onExpandedChange = { expandedMenuStyle = !expandedMenuStyle }
                    ) {
                        OutlinedTextField(
                            value = menuStyle,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Menu Style") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenuStyle) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0xFF33334D),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = SpaceDark,
                                unfocusedContainerColor = SpaceDark
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMenuStyle,
                            onDismissRequest = { expandedMenuStyle = false }
                        ) {
                            BotConfig.MENU_STYLES.forEach { style ->
                                DropdownMenuItem(
                                    text = { Text(style) },
                                    onClick = {
                                        menuStyle = style
                                        expandedMenuStyle = false
                                    }
                                )
                            }
                        }
                    }

                    // Theme Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedTheme,
                        onExpandedChange = { expandedTheme = !expandedTheme }
                    ) {
                        OutlinedTextField(
                            value = theme,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Theme Visual") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTheme) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0xFF33334D),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = SpaceDark,
                                unfocusedContainerColor = SpaceDark
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTheme,
                            onDismissRequest = { expandedTheme = false }
                        ) {
                            BotConfig.THEMES.forEach { th ->
                                DropdownMenuItem(
                                    text = { Text(th) },
                                    onClick = {
                                        theme = th
                                        expandedTheme = false
                                    }
                                )
                            }
                        }
                    }

                    // Language Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedLang,
                        onExpandedChange = { expandedLang = !expandedLang }
                    ) {
                        OutlinedTextField(
                            value = language,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Language (Bahasa)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLang) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0xFF33334D),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = SpaceDark,
                                unfocusedContainerColor = SpaceDark
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedLang,
                            onDismissRequest = { expandedLang = false }
                        ) {
                            BotConfig.LANGUAGES.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text(lang) },
                                    onClick = {
                                        language = lang
                                        expandedLang = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 5: Sistem & Keamanan
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "5. SISTEM & KEAMANAN",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ToggleRow(
                        title = "Auto Reply",
                        desc = "Balas otomatis pesan yang tidak dikenali dengan info menu",
                        checked = autoReply,
                        onCheckedChange = { autoReply = it }
                    )

                    ToggleRow(
                        title = "Anti Spam Protection",
                        desc = "Batasi pengiriman pesan beruntun yang cepat dari user",
                        checked = antiSpam,
                        onCheckedChange = { antiSpam = it }
                    )

                    ToggleRow(
                        title = "Maintenance Mode",
                        desc = "Kunci akses umum saat perbaikan sistem sedang berlangsung",
                        checked = maintenanceMode,
                        onCheckedChange = { maintenanceMode = it }
                    )
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    viewModel.updateConfig(
                        BotConfig(
                            botName = botName.trim(),
                            botUsername = botUsername.trim(),
                            prefix = prefix.trim(),
                            ownerName = ownerName.trim(),
                            ownerId = ownerId.trim(),
                            welcomeMessage = welcomeMessage.trim(),
                            goodbyeMessage = goodbyeMessage.trim(),
                            menuStyle = menuStyle,
                            theme = theme,
                            language = language,
                            autoReply = autoReply,
                            antiSpam = antiSpam,
                            maintenanceMode = maintenanceMode
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Simpan Konfigurasi Bot", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ConfigTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Column {
        Text(text = label, color = Color(0xFFA0AEC0), fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = Color(0xFF33334D),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = SpaceDark,
                unfocusedContainerColor = SpaceDark
            )
        )
    }
}

@Composable
private fun ToggleRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = desc, color = Color.Gray, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = NeonViolet)
        )
    }
}
