package cz.craftmania.logger

import cz.craftmania.logger.listeners.*
import cz.craftmania.logger.sql.SQLManager
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    var isDebugEnabled = false
        private set
    var serverId = "test"
        private set
    var luckPermsApi: LuckPerms? = null
        private set
    var sQL: SQLManager? = null
        private set
    var isVipChangesEnabled = false
        private set
    var isEconomyChangesEnabled = false
        private set
    var isLevelsChangesEnabled = false
        private set
    var isCommandLoggerEnabled = false
        private set
    var isChatLoggerEnabled = false
        private set
    var isJoinAndLeaveEnabled = false
        private set

    override fun onEnable() {

        // Instance
        instance = this

        //Config
        config.options().copyDefaults(true)
        saveDefaultConfig()
        isDebugEnabled = config.getBoolean("debug", false)
        serverId = config.getString("server", "test")!!
        isVipChangesEnabled = config.getBoolean("logger.vip-status", false)
        isEconomyChangesEnabled = config.getBoolean("logger.economy-changes", false)
        isLevelsChangesEnabled = config.getBoolean("logger.levels-change", false)
        isCommandLoggerEnabled = config.getBoolean("logger.commands.enabled", false)
        isChatLoggerEnabled = config.getBoolean("logger.chat-logs", false)
        isJoinAndLeaveEnabled = config.getBoolean("logger.login-logout", false)

        // HikariCP
        sQL = SQLManager(this)

        // Plugin loader
        val pluginManager = server.pluginManager

        // VIP changes setting
        if (isVipChangesEnabled) {

            // LuckPerms register
            val provider = Bukkit.getServicesManager().getRegistration(
                LuckPerms::class.java
            )
            if (provider != null) {
                luckPermsApi = provider.provider
            }
            pluginManager.registerEvents(LuckPermsListener(), this)
        }

        // Economy changes
        if (isEconomyChangesEnabled) {
            pluginManager.registerEvents(EconomyChangesListener(), this)
        }
        if (isLevelsChangesEnabled) {
            pluginManager.registerEvents(EconomyLevelUpListener(), this)
        }
        if (isCommandLoggerEnabled) {
            pluginManager.registerEvents(CommandLogListener(), this)
        }
        if (isChatLoggerEnabled) {
            pluginManager.registerEvents(ChatLoggerListener(), this)
        }
        if (isJoinAndLeaveEnabled) {
            pluginManager.registerEvents(JoinAndLeaveLoggerListener(), this)
        }
    }

    override fun onDisable() {

        // Deaktivace MySQL
        sQL!!.onDisable()
        instance = null
    }

    companion object {

        var instance: Main? = null
            private set
    }
}