package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BgrGraffitiLogoBadge
import com.example.ui.components.BlackHoleLoadingScreen
import com.example.ui.components.FullMusicPlayerDialog
import com.example.ui.components.GalaxyBackground
import com.example.ui.components.MiniMusicPlayerBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.BotConfigScreen
import com.example.ui.screens.FeaturesScreen
import com.example.ui.screens.GeneratorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InfoScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.PreviewScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BotGeneratorTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.SpaceDark
import com.example.viewmodel.BotGeneratorViewModel
import com.example.viewmodel.MainTab
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: BotGeneratorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotGeneratorTheme {
                var isLoading by remember { mutableStateOf(true) }

                if (isLoading) {
                    BlackHoleLoadingScreen(
                        onLoadingComplete = { isLoading = false }
                    )
                } else {
                    MainAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: BotGeneratorViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isPlaying by viewModel.musicPlayer.isPlaying.collectAsState()
    val currentPosition by viewModel.musicPlayer.currentPosition.collectAsState()
    val volume by viewModel.musicPlayer.volume.collectAsState()
    val currentTrackIndex by viewModel.musicPlayer.currentTrackIndex.collectAsState()
    val snackbarMsg by viewModel.snackbarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showFullMusicPlayer by remember { mutableStateOf(false) }
    var showMoreMenuSheet by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle back button when in secondary screen
    if (currentTab != MainTab.HOME) {
        BackHandler {
            viewModel.setTab(MainTab.HOME)
        }
    }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissSnackbar()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SpaceDark,
                modifier = Modifier.width(300.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BgrGraffitiLogoBadge(size = 46.dp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "  BOT GENERATOR • ARDZYN",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // All 10 menu destinations
                val allMenuTabs = listOf(
                    MainTab.HOME to Icons.Default.Home,
                    MainTab.GENERATOR to Icons.Default.Code,
                    MainTab.FEATURES to Icons.Default.Tune,
                    MainTab.PREVIEW to Icons.Default.Chat,
                    MainTab.PROJECTS to Icons.Default.Folder,
                    MainTab.SETTINGS to Icons.Default.Settings,
                    MainTab.ABOUT to Icons.Default.Info,
                    MainTab.AKUN to Icons.Default.Person,
                    MainTab.INFORMATION to Icons.Default.HelpCenter,
                    MainTab.BERITA to Icons.Default.Newspaper
                )

                allMenuTabs.forEach { (tab, icon) ->
                    val isSelected = currentTab == tab
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = icon, contentDescription = null, tint = if (isSelected) NeonCyan else Color.Gray) },
                        label = {
                            Text(
                                text = when (tab) {
                                    MainTab.FEATURES -> "350 Features"
                                    MainTab.ABOUT -> "About & Creator (ARDZYN)"
                                    else -> tab.title
                                },
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            viewModel.setTab(tab)
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NeonViolet.copy(alpha = 0.25f),
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                    )
                }
            }
        }
    ) {
        GalaxyBackground {
            Scaffold(
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1B1B30))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Drawer Menu",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = currentTab.title.uppercase(),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black)
                                        .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                                        .clickable { viewModel.setTab(MainTab.AKUN) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "ARDZYN",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                },
                bottomBar = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Docked Mini Music Player
                        MiniMusicPlayerBar(
                            player = viewModel.musicPlayer,
                            isPlaying = isPlaying,
                            currentPosition = currentPosition,
                            onClick = { showFullMusicPlayer = true }
                        )

                        // Bottom Navigation Bar
                        NavigationBar(
                            containerColor = SpaceDark.copy(alpha = 0.95f),
                            contentColor = Color.White,
                            modifier = Modifier.height(64.dp)
                        ) {
                            val bottomTabs = listOf(
                                MainTab.HOME to Icons.Default.Home,
                                MainTab.GENERATOR to Icons.Default.Code,
                                MainTab.FEATURES to Icons.Default.Tune,
                                MainTab.PREVIEW to Icons.Default.Chat,
                                MainTab.SETTINGS to Icons.Default.Settings
                            )

                            bottomTabs.forEach { (tab, icon) ->
                                val isSelected = currentTab == tab
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { viewModel.setTab(tab) },
                                    icon = {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) NeonCyan else Color.Gray,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = if (tab == MainTab.FEATURES) "350" else tab.title,
                                            fontSize = 10.sp,
                                            color = if (isSelected) NeonCyan else Color.Gray,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NeonCyan,
                                        indicatorColor = NeonViolet.copy(alpha = 0.25f)
                                    ),
                                    modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
                                )
                            }

                            // More Menu Trigger
                            NavigationBarItem(
                                selected = false,
                                onClick = { showMoreMenuSheet = true },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "More Menu",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text("Menu", fontSize = 10.sp, color = Color.Gray)
                                },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        MainTab.HOME -> HomeScreen(
                            viewModel = viewModel,
                            onOpenMusicPlayer = { showFullMusicPlayer = true }
                        )
                        MainTab.GENERATOR -> GeneratorScreen(viewModel = viewModel)
                        MainTab.FEATURES -> FeaturesScreen(viewModel = viewModel)
                        MainTab.PREVIEW -> PreviewScreen(viewModel = viewModel)
                        MainTab.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                        MainTab.SETTINGS -> BotConfigScreen(viewModel = viewModel)
                        MainTab.ABOUT -> AboutScreen()
                        MainTab.AKUN -> AccountScreen(viewModel = viewModel)
                        MainTab.INFORMATION -> InfoScreen()
                        MainTab.BERITA -> NewsScreen()
                    }
                }
            }
        }
    }

    // Full Music Player Modal Dialog
    if (showFullMusicPlayer) {
        FullMusicPlayerDialog(
            player = viewModel.musicPlayer,
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            volume = volume,
            currentTrackIndex = currentTrackIndex,
            onDismiss = { showFullMusicPlayer = false }
        )
    }

    // More Menu Bottom Sheet
    if (showMoreMenuSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMoreMenuSheet = false },
            containerColor = SpaceDark,
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "MENU NAVIGASI LENGKAP",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )

                val extraItems = listOf(
                    Triple(MainTab.PROJECTS, Icons.Default.Folder, "Project Manager & JSON Editor"),
                    Triple(MainTab.SETTINGS, Icons.Default.Settings, "Bot Configuration & Toggles"),
                    Triple(MainTab.ABOUT, Icons.Default.Info, "Tentang BOT GENERATOR"),
                    Triple(MainTab.AKUN, Icons.Default.Person, "Akun Developer (ARDZYN)"),
                    Triple(MainTab.INFORMATION, Icons.Default.HelpCenter, "Panduan VPS & API Token"),
                    Triple(MainTab.BERITA, Icons.Default.Newspaper, "Berita & Catatan Rilis")
                )

                extraItems.forEach { (tab, icon, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF14162B))
                            .clickable {
                                viewModel.setTab(tab)
                                showMoreMenuSheet = false
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = tab.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(text = desc, color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
