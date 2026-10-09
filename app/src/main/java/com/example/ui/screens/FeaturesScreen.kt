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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BotFeature
import com.example.data.model.FeatureCategory
import com.example.data.model.FeaturePermission
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SpaceDark
import com.example.viewmodel.BotGeneratorViewModel

@Composable
fun FeaturesScreen(viewModel: BotGeneratorViewModel) {
    val project by viewModel.currentProject.collectAsState()
    val features by viewModel.filteredFeatures.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filterActiveOnly by viewModel.filterActiveOnly.collectAsState()
    val editingFeature by viewModel.editingFeature.collectAsState()

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
                    text = "FEATURE MANAGER",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Total 350 Fitur • Aktif: ${project.features.count { it.isEnabled }}",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E1E34))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${features.size} Ditampilkan",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari dari 350 fitur (nama, command, atau deskripsi)...", fontSize = 12.sp) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = NeonViolet.copy(alpha = 0.4f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = SpaceDark,
                unfocusedContainerColor = SpaceDark
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips Horizontal Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.selectCategory(null) },
                    label = { Text("Semua (350)", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = Color.White,
                        containerColor = SpaceDark,
                        labelColor = Color.Gray
                    )
                )
            }

            item {
                FilterChip(
                    selected = filterActiveOnly,
                    onClick = { viewModel.toggleFilterActiveOnly() },
                    label = { Text("Hanya Aktif", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan,
                        selectedLabelColor = Color.Black,
                        containerColor = SpaceDark,
                        labelColor = Color.Gray
                    )
                )
            }

            items(FeatureCategory.values()) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectCategory(if (isSelected) null else cat) },
                    label = { Text(cat.displayName, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = Color.White,
                        containerColor = SpaceDark,
                        labelColor = Color(0xFFCBD5E1)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bulk Actions Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.enableAllFeatures() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A2F)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Aktifkan Semua", fontSize = 11.sp, color = Color(0xFF4ADE80))
            }

            Button(
                onClick = { viewModel.disableAllFeatures() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381E2F)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Matikan Semua", fontSize = 11.sp, color = NeonPink)
            }

            if (selectedCategory != null) {
                Button(
                    onClick = { viewModel.enableCategoryFeatures(selectedCategory!!) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF281E48)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Aktifkan ${selectedCategory!!.displayName}", fontSize = 10.sp, color = NeonViolet)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Features List (1 to 350)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(features, key = { it.id }) { feat ->
                FeatureItemCard(
                    feature = feat,
                    botPrefix = project.config.prefix,
                    onToggle = { viewModel.toggleFeature(feat.id) },
                    onEdit = { viewModel.openEditFeature(feat) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Detail Edit Feature Dialog
    editingFeature?.let { feat ->
        EditFeatureDialog(
            feature = feat,
            onDismiss = { viewModel.closeEditFeature() },
            onSave = { viewModel.saveEditedFeature(it) }
        )
    }
}

@Composable
fun FeatureItemCard(
    feature: BotFeature,
    botPrefix: String,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    val displayPrefix = if (feature.prefix.isNotBlank()) feature.prefix else botPrefix

    GlassCard(
        borderColor = if (feature.isEnabled) NeonViolet.copy(alpha = 0.5f) else Color(0x33444466),
        backgroundColor = if (feature.isEnabled) Color(0xDD0E1022) else Color(0x990A0B16),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${feature.id}",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(36.dp)
                    )

                    Text(
                        text = feature.name,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1B2038))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$displayPrefix${feature.command}",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = feature.description,
                    color = Color(0xFFA0AEC0),
                    fontSize = 12.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TagBadge(text = feature.category.displayName, color = NeonViolet)
                    TagBadge(text = feature.permission.displayName, color = Color(0xFFF59E0B))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Feature",
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Switch(
                    checked = feature.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = NeonViolet,
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color(0xFF222238)
                    )
                )
            }
        }
    }
}

@Composable
private fun TagBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.15f))
            .border(0.8.dp, color.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFeatureDialog(
    feature: BotFeature,
    onDismiss: () -> Unit,
    onSave: (BotFeature) -> Unit
) {
    var name by remember { mutableStateOf(feature.name) }
    var command by remember { mutableStateOf(feature.command) }
    var prefix by remember { mutableStateOf(feature.prefix) }
    var description by remember { mutableStateOf(feature.description) }
    var response by remember { mutableStateOf(feature.response) }
    var selectedCategory by remember { mutableStateOf(feature.category) }
    var selectedPermission by remember { mutableStateOf(feature.permission) }
    var isEnabled by remember { mutableStateOf(feature.isEnabled) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .background(SpaceDark)
                .border(1.5.dp, NeonViolet, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "EDIT FITUR #${feature.id}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Kustomisasi parameter dan response bot",
                        color = NeonCyan,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                item {
                    DialogField(label = "Nama Fitur", value = name, onValueChange = { name = it })
                }

                item {
                    DialogField(label = "Command (Tanpa spasi)", value = command, onValueChange = { command = it })
                }

                item {
                    DialogField(label = "Custom Prefix (Opsional)", value = prefix, onValueChange = { prefix = it })
                }

                item {
                    DialogField(label = "Deskripsi Fitur", value = description, onValueChange = { description = it })
                }

                item {
                    DialogField(
                        label = "Template Respon Bot",
                        value = response,
                        onValueChange = { response = it },
                        singleLine = false
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status Aktif", color = Color.White, fontSize = 13.sp)
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = { isEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = NeonViolet)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222238))
                        ) {
                            Text("Batal", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onSave(
                                    feature.copy(
                                        name = name,
                                        command = command.trim(),
                                        prefix = prefix.trim(),
                                        description = description,
                                        response = response,
                                        category = selectedCategory,
                                        permission = selectedPermission,
                                        isEnabled = isEnabled
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                        ) {
                            Text("Simpan", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogField(
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
                focusedContainerColor = Color(0xFF101222),
                unfocusedContainerColor = Color(0xFF101222)
            )
        )
    }
}
