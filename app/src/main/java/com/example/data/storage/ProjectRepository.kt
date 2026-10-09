package com.example.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.data.features.DefaultFeaturesCatalog
import com.example.data.model.BotConfig
import com.example.data.model.BotFeature
import com.example.data.model.BotPlatform
import com.example.data.model.BotProject
import com.example.data.model.FeatureCategory
import com.example.data.model.FeaturePermission
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ProjectRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bgr_bot_projects_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PROJECT_LIST = "saved_projects_json_list"
        private const val KEY_ACTIVE_PROJECT_ID = "active_project_id"
    }

    fun getActiveProjectId(): String? {
        return prefs.getString(KEY_ACTIVE_PROJECT_ID, null)
    }

    fun setActiveProjectId(id: String) {
        prefs.edit().putString(KEY_ACTIVE_PROJECT_ID, id).apply()
    }

    fun loadAllProjects(): List<BotProject> {
        val rawJson = prefs.getString(KEY_PROJECT_LIST, null)
        if (rawJson.isNullOrBlank()) {
            val defaultProject = createDefaultStarterProject()
            saveAllProjects(listOf(defaultProject))
            setActiveProjectId(defaultProject.id)
            return listOf(defaultProject)
        }

        return try {
            val jsonArray = JSONArray(rawJson)
            val result = mutableListOf<BotProject>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                result.add(deserializeProject(obj))
            }
            if (result.isEmpty()) {
                val defaultProject = createDefaultStarterProject()
                saveAllProjects(listOf(defaultProject))
                listOf(defaultProject)
            } else {
                result
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val fallback = createDefaultStarterProject()
            listOf(fallback)
        }
    }

    fun saveProject(project: BotProject) {
        val all = loadAllProjects().toMutableList()
        val index = all.indexOfFirst { it.id == project.id }
        if (index >= 0) {
            all[index] = project.copy(updatedAt = System.currentTimeMillis())
        } else {
            all.add(project.copy(updatedAt = System.currentTimeMillis()))
        }
        saveAllProjects(all)
    }

    fun duplicateProject(sourceId: String): BotProject? {
        val all = loadAllProjects().toMutableList()
        val source = all.find { it.id == sourceId } ?: return null
        val copy = source.copy(
            id = UUID.randomUUID().toString(),
            name = "${source.name} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        all.add(copy)
        saveAllProjects(all)
        return copy
    }

    fun deleteProject(projectId: String): Boolean {
        val all = loadAllProjects().toMutableList()
        if (all.size <= 1) {
            // Keep at least one project
            return false
        }
        val removed = all.removeAll { it.id == projectId }
        if (removed) {
            saveAllProjects(all)
            if (getActiveProjectId() == projectId) {
                setActiveProjectId(all.first().id)
            }
        }
        return removed
    }

    fun createNewProject(name: String, platform: BotPlatform): BotProject {
        val newProj = BotProject(
            id = UUID.randomUUID().toString(),
            name = name,
            platform = platform,
            config = BotConfig(
                botName = name,
                prefix = platform.defaultPrefix,
                ownerName = "ARDZYN"
            ),
            features = DefaultFeaturesCatalog.createInitial350Features()
        )
        saveProject(newProj)
        setActiveProjectId(newProj.id)
        return newProj
    }

    private fun saveAllProjects(list: List<BotProject>) {
        val jsonArray = JSONArray()
        list.forEach { project ->
            jsonArray.put(serializeProject(project))
        }
        prefs.edit().putString(KEY_PROJECT_LIST, jsonArray.toString()).apply()
    }

    fun exportProjectToJson(project: BotProject): String {
        return serializeProject(project).toString(2)
    }

    fun importProjectFromJson(jsonString: String): BotProject? {
        return try {
            val jsonObject = JSONObject(jsonString)
            val project = deserializeProject(jsonObject).copy(
                id = UUID.randomUUID().toString(),
                updatedAt = System.currentTimeMillis()
            )
            saveProject(project)
            setActiveProjectId(project.id)
            project
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createDefaultStarterProject(): BotProject {
        return BotProject(
            id = "default_bgr_starter_01",
            name = "BGR Nexus Master Bot",
            platform = BotPlatform.WHATSAPP,
            config = BotConfig(
                botName = "BGR NEXUS BOT",
                botUsername = "@bgr_nexus_bot",
                prefix = "!",
                ownerName = "ARDZYN",
                ownerId = "6289512345678",
                welcomeMessage = "Halo Selamat datang di server/grup! Patuhi peraturan dan selamat beraktivitas.",
                goodbyeMessage = "Sampai jumpa kawan! Semoga sukses di perjalanan luar angkasamu.",
                menuStyle = "Bubble Graffiti",
                theme = "Galaxy Violet",
                language = "Indonesian",
                autoReply = true,
                antiSpam = true,
                maintenanceMode = false
            ),
            features = DefaultFeaturesCatalog.createInitial350Features()
        )
    }

    fun serializeProject(project: BotProject): JSONObject {
        val root = JSONObject()
        root.put("id", project.id)
        root.put("name", project.name)
        root.put("platform", project.platform.name)
        root.put("createdAt", project.createdAt)
        root.put("updatedAt", project.updatedAt)

        val cfg = JSONObject()
        cfg.put("botName", project.config.botName)
        cfg.put("botUsername", project.config.botUsername)
        cfg.put("prefix", project.config.prefix)
        cfg.put("ownerName", project.config.ownerName)
        cfg.put("ownerId", project.config.ownerId)
        cfg.put("welcomeMessage", project.config.welcomeMessage)
        cfg.put("goodbyeMessage", project.config.goodbyeMessage)
        cfg.put("menuStyle", project.config.menuStyle)
        cfg.put("theme", project.config.theme)
        cfg.put("language", project.config.language)
        cfg.put("autoReply", project.config.autoReply)
        cfg.put("antiSpam", project.config.antiSpam)
        cfg.put("maintenanceMode", project.config.maintenanceMode)
        root.put("config", cfg)

        val featArray = JSONArray()
        project.features.forEach { f ->
            val fo = JSONObject()
            fo.put("id", f.id)
            fo.put("name", f.name)
            fo.put("command", f.command)
            fo.put("prefix", f.prefix)
            fo.put("description", f.description)
            fo.put("category", f.category.name)
            fo.put("permission", f.permission.name)
            fo.put("response", f.response)
            fo.put("isEnabled", f.isEnabled)
            featArray.put(fo)
        }
        root.put("features", featArray)
        return root
    }

    fun deserializeProject(root: JSONObject): BotProject {
        val id = root.optString("id", UUID.randomUUID().toString())
        val name = root.optString("name", "Bot Project")
        val platformStr = root.optString("platform", "WHATSAPP")
        val platform = try {
            BotPlatform.valueOf(platformStr)
        } catch (e: Exception) {
            BotPlatform.WHATSAPP
        }
        val createdAt = root.optLong("createdAt", System.currentTimeMillis())
        val updatedAt = root.optLong("updatedAt", System.currentTimeMillis())

        val cfgObj = root.optJSONObject("config")
        val config = if (cfgObj != null) {
            BotConfig(
                botName = cfgObj.optString("botName", "BGR NEXUS BOT"),
                botUsername = cfgObj.optString("botUsername", "@bgr_nexus_bot"),
                prefix = cfgObj.optString("prefix", "!"),
                ownerName = cfgObj.optString("ownerName", "ARDZYN"),
                ownerId = cfgObj.optString("ownerId", "6289512345678"),
                welcomeMessage = cfgObj.optString("welcomeMessage", "Selamat datang!"),
                goodbyeMessage = cfgObj.optString("goodbyeMessage", "Sampai jumpa!"),
                menuStyle = cfgObj.optString("menuStyle", "Bubble Graffiti"),
                theme = cfgObj.optString("theme", "Galaxy Violet"),
                language = cfgObj.optString("language", "Indonesian"),
                autoReply = cfgObj.optBoolean("autoReply", true),
                antiSpam = cfgObj.optBoolean("antiSpam", true),
                maintenanceMode = cfgObj.optBoolean("maintenanceMode", false)
            )
        } else {
            BotConfig()
        }

        val featArray = root.optJSONArray("features")
        val features = mutableListOf<BotFeature>()
        if (featArray != null && featArray.length() > 0) {
            for (i in 0 until featArray.length()) {
                val fo = featArray.getJSONObject(i)
                val catStr = fo.optString("category", "GENERAL")
                val category = try {
                    FeatureCategory.valueOf(catStr)
                } catch (e: Exception) {
                    FeatureCategory.GENERAL
                }
                val permStr = fo.optString("permission", "PUBLIC")
                val permission = try {
                    FeaturePermission.valueOf(permStr)
                } catch (e: Exception) {
                    FeaturePermission.PUBLIC
                }

                features.add(
                    BotFeature(
                        id = fo.optInt("id", i + 1),
                        name = fo.optString("name", "Fitur #${i + 1}"),
                        command = fo.optString("command", "cmd${i + 1}"),
                        prefix = fo.optString("prefix", ""),
                        description = fo.optString("description", ""),
                        category = category,
                        permission = permission,
                        response = fo.optString("response", "Berhasil dieksekusi."),
                        isEnabled = fo.optBoolean("isEnabled", true)
                    )
                )
            }
        } else {
            features.addAll(DefaultFeaturesCatalog.createInitial350Features())
        }

        return BotProject(
            id = id,
            name = name,
            platform = platform,
            config = config,
            features = features,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
