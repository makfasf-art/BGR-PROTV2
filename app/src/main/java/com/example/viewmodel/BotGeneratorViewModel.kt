package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.ProceduralMusicPlayer
import com.example.data.model.BotConfig
import com.example.data.model.BotFeature
import com.example.data.model.BotPlatform
import com.example.data.model.BotProject
import com.example.data.model.FeatureCategory
import com.example.data.model.FeaturePermission
import com.example.data.storage.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab(val title: String, val iconName: String) {
    HOME("Home", "home"),
    GENERATOR("Generator", "code"),
    FEATURES("Features", "tune"),
    PREVIEW("Preview", "chat"),
    PROJECTS("Projects", "folder"),
    SETTINGS("Settings", "settings"),
    ABOUT("About", "info"),
    AKUN("Akun", "person"),
    INFORMATION("Info", "help_center"),
    BERITA("Berita", "newspaper")
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String,
    val text: String,
    val isBot: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class BotGeneratorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProjectRepository(application)
    val musicPlayer = ProceduralMusicPlayer()

    private val _currentProject = MutableStateFlow<BotProject>(
        repository.loadAllProjects().firstOrNull() ?: repository.createNewProject("BGR Nexus Master Bot", BotPlatform.WHATSAPP)
    )
    val currentProject: StateFlow<BotProject> = _currentProject.asStateFlow()

    private val _allProjects = MutableStateFlow<List<BotProject>>(repository.loadAllProjects())
    val allProjects: StateFlow<List<BotProject>> = _allProjects.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<FeatureCategory?>(null)
    val selectedCategory: StateFlow<FeatureCategory?> = _selectedCategory.asStateFlow()

    private val _filterActiveOnly = MutableStateFlow(false)
    val filterActiveOnly: StateFlow<Boolean> = _filterActiveOnly.asStateFlow()

    private val _editingFeature = MutableStateFlow<BotFeature?>(null)
    val editingFeature: StateFlow<BotFeature?> = _editingFeature.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _jsonEditorText = MutableStateFlow("")
    val jsonEditorText: StateFlow<String> = _jsonEditorText.asStateFlow()

    val filteredFeatures: StateFlow<List<BotFeature>> = combine(
        _currentProject,
        _searchQuery,
        _selectedCategory,
        _filterActiveOnly
    ) { proj, query, category, activeOnly ->
        proj.features.filter { feat ->
            val matchesQuery = query.isBlank() ||
                feat.name.contains(query, ignoreCase = true) ||
                feat.command.contains(query, ignoreCase = true) ||
                feat.description.contains(query, ignoreCase = true)

            val matchesCat = category == null || feat.category == category
            val matchesActive = !activeOnly || feat.isEnabled

            matchesQuery && matchesCat && matchesActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        initChatSession()
        refreshJsonEditorText()
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun selectCategory(cat: FeatureCategory?) {
        _selectedCategory.value = cat
    }

    fun toggleFilterActiveOnly() {
        _filterActiveOnly.value = !_filterActiveOnly.value
    }

    fun selectPlatform(platform: BotPlatform) {
        val updated = _currentProject.value.copy(
            platform = platform,
            config = _currentProject.value.config.copy(prefix = platform.defaultPrefix)
        )
        updateProject(updated)
        showSnackbar("Platform diganti ke ${platform.displayName}")
    }

    fun updateConfig(newConfig: BotConfig) {
        val updated = _currentProject.value.copy(config = newConfig)
        updateProject(updated)
        showSnackbar("Konfigurasi bot disimpan!")
    }

    fun toggleFeature(featureId: Int) {
        val features = _currentProject.value.features.map { f ->
            if (f.id == featureId) f.copy(isEnabled = !f.isEnabled) else f
        }
        val updated = _currentProject.value.copy(features = features)
        updateProject(updated)
    }

    fun openEditFeature(feature: BotFeature) {
        _editingFeature.value = feature
    }

    fun closeEditFeature() {
        _editingFeature.value = null
    }

    fun saveEditedFeature(feature: BotFeature) {
        val features = _currentProject.value.features.map { f ->
            if (f.id == feature.id) feature else f
        }
        val updated = _currentProject.value.copy(features = features)
        updateProject(updated)
        _editingFeature.value = null
        showSnackbar("Fitur '${feature.name}' berhasil diperbarui.")
    }

    fun enableAllFeatures() {
        val features = _currentProject.value.features.map { it.copy(isEnabled = true) }
        val updated = _currentProject.value.copy(features = features)
        updateProject(updated)
        showSnackbar("Semua 350 fitur diaktifkan!")
    }

    fun disableAllFeatures() {
        val features = _currentProject.value.features.map { it.copy(isEnabled = false) }
        val updated = _currentProject.value.copy(features = features)
        updateProject(updated)
        showSnackbar("Semua fitur dinonaktifkan.")
    }

    fun enableCategoryFeatures(category: FeatureCategory) {
        val features = _currentProject.value.features.map {
            if (it.category == category) it.copy(isEnabled = true) else it
        }
        val updated = _currentProject.value.copy(features = features)
        updateProject(updated)
        showSnackbar("Semua fitur kategori ${category.displayName} diaktifkan.")
    }

    // Project Manager Actions
    fun createProject(name: String, platform: BotPlatform) {
        val created = repository.createNewProject(name, platform)
        _allProjects.value = repository.loadAllProjects()
        _currentProject.value = created
        refreshJsonEditorText()
        showSnackbar("Project '$name' berhasil dibuat.")
    }

    fun switchProject(project: BotProject) {
        repository.setActiveProjectId(project.id)
        _currentProject.value = project
        refreshJsonEditorText()
        initChatSession()
        showSnackbar("Project aktif: ${project.name}")
    }

    fun duplicateCurrentProject() {
        val copy = repository.duplicateProject(_currentProject.value.id)
        if (copy != null) {
            _allProjects.value = repository.loadAllProjects()
            _currentProject.value = copy
            refreshJsonEditorText()
            showSnackbar("Project berhasil diduplikasi!")
        }
    }

    fun deleteProject(projectId: String) {
        val deleted = repository.deleteProject(projectId)
        if (deleted) {
            _allProjects.value = repository.loadAllProjects()
            _currentProject.value = _allProjects.value.first()
            refreshJsonEditorText()
            showSnackbar("Project berhasil dihapus.")
        } else {
            showSnackbar("Gagal: Minimal harus ada satu project tersisa.")
        }
    }

    fun exportCurrentJson(): String {
        return repository.exportProjectToJson(_currentProject.value)
    }

    fun importJson(json: String): Boolean {
        val imported = repository.importProjectFromJson(json)
        return if (imported != null) {
            _allProjects.value = repository.loadAllProjects()
            _currentProject.value = imported
            refreshJsonEditorText()
            showSnackbar("Konfigurasi JSON berhasil diimpor!")
            true
        } else {
            showSnackbar("Format JSON tidak valid!")
            false
        }
    }

    fun setJsonEditorText(text: String) {
        _jsonEditorText.value = text
    }

    fun applyJsonFromEditor(): Boolean {
        return importJson(_jsonEditorText.value)
    }

    private fun refreshJsonEditorText() {
        _jsonEditorText.value = repository.exportProjectToJson(_currentProject.value)
    }

    private fun updateProject(project: BotProject) {
        _currentProject.value = project
        repository.saveProject(project)
        _allProjects.value = repository.loadAllProjects()
        refreshJsonEditorText()
    }

    // Bot Chat Preview Simulation
    private fun initChatSession() {
        val p = _currentProject.value
        _chatMessages.value = listOf(
            ChatMessage(
                sender = "SYSTEM",
                text = "⚡ Simulasi ${p.platform.displayName} terhubung. Coba ketik command dengan prefix '${p.config.prefix}' (contoh: ${p.config.prefix}menu atau ${p.config.prefix}ping).",
                isBot = true
            )
        )
    }

    fun sendUserMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = "User", text = text, isBot = false)
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMsg)
        _chatMessages.value = currentList

        // Generate Bot Response
        val proj = _currentProject.value
        val prefix = proj.config.prefix
        val clean = text.trim()

        val botReplyText: String = if (clean.startsWith(prefix)) {
            val cmdName = clean.removePrefix(prefix).trim().split(" ")[0].lowercase()

            if (cmdName == "menu" || cmdName == "help" || cmdName == "start") {
                buildSimulatedMenuText(proj)
            } else {
                val found = proj.features.find { it.command.equals(cmdName, ignoreCase = true) }
                if (found != null) {
                    if (found.isEnabled) {
                        "✅ [${found.name}]\n${found.response}"
                    } else {
                        "⚠️ Perintah '${found.command}' sedang dinonaktifkan oleh owner di Bot Generator."
                    }
                } else {
                    if (proj.config.autoReply) {
                        "❌ Perintah '$cmdName' tidak ditemukan. Ketik '${prefix}menu' untuk melihat daftar fitur aktif."
                    } else {
                        "..."
                    }
                }
            }
        } else {
            if (proj.config.autoReply) {
                "Halo! Saya ${proj.config.botName}. Gunakan prefix '${prefix}' untuk memanggil perintah saya."
            } else {
                "..."
            }
        }

        viewModelScope.launch {
            kotlinx.coroutines.delay(400) // realistic typing delay
            val botMsg = ChatMessage(
                sender = proj.config.botName,
                text = botReplyText,
                isBot = true
            )
            val updated = _chatMessages.value.toMutableList()
            updated.add(botMsg)
            _chatMessages.value = updated
        }
    }

    private fun buildSimulatedMenuText(proj: BotProject): String {
        val active = proj.features.filter { it.isEnabled }
        val sb = StringBuilder()
        when (proj.config.menuStyle) {
            "Bubble Graffiti" -> {
                sb.append("╭━━━━❰ ${proj.config.botName} ❱━━━━╮\n")
                sb.append("┃ 🎨 Style: BGR Bubble Graffiti\n")
                sb.append("┃ 👑 Owner: ${proj.config.ownerName}\n")
                sb.append("┃ ⚡ Prefix: [ ${proj.config.prefix} ]\n")
                sb.append("┃ 🚀 Fitur Aktif: ${active.size} / 350\n")
                sb.append("╰━━━━━━━━━━━━━━━━━━━━━━╯\n\n")
            }
            "Modern Cyberpunk" -> {
                sb.append("╔════[ ${proj.config.botName} // CYBER ]════╗\n")
                sb.append("║ AUTH: ${proj.config.ownerName} | STATUS: ONLINE\n")
                sb.append("║ CORES: ${active.size} ACTIVE MODULES\n")
                sb.append("╚══════════════════════════════╝\n\n")
            }
            else -> {
                sb.append("🌟 *${proj.config.botName}*\n")
                sb.append("Prefix: `${proj.config.prefix}` | Owner: ${proj.config.ownerName}\n")
                sb.append("Total Modul Aktif: ${active.size}\n\n")
            }
        }

        val grouped = active.groupBy { it.category }
        grouped.forEach { (cat, feats) ->
            sb.append("📁 *${cat.displayName}* (${feats.size}):\n")
            feats.take(8).forEach { f ->
                sb.append(" • `${proj.config.prefix}${f.command}` - ${f.name}\n")
            }
            if (feats.size > 8) {
                sb.append(" • _...dan ${feats.size - 8} fitur lainnya_\n")
            }
            sb.append("\n")
        }
        sb.append("Powered by BGR PRJCT Bot Generator")
        return sb.toString()
    }

    fun clearChat() {
        initChatSession()
        showSnackbar("Simulasi chat dibersihkan.")
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun dismissSnackbar() {
        _snackbarMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        musicPlayer.release()
    }
}
