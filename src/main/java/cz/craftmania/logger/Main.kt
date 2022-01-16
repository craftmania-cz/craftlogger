package cz.craftmania.logger

import cz.craftmania.logger.listeners.external.*
import cz.craftmania.logger.listeners.internal.ChatLoggerListener
import cz.craftmania.logger.listeners.internal.CommandLogListener
import cz.craftmania.logger.listeners.internal.JoinAndLeaveLoggerListener
import cz.craftmania.logger.sql.SQLManager
import cz.craftmania.logger.utils.Log
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
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
    var isVotePartyLoggedEnabled = false
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
        isVotePartyLoggedEnabled = config.getBoolean("logger.voteparty", false)
        Log.withPrefix("Server je registrovaný jako: $serverId")

        // HikariCP
        sQL = SQLManager(this)
        Log.withPrefix("Aktivace MySQL připojení.")

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
            Log.withPrefix("Server bude ukládat a aktualizovat VIP v Ccomunity.")
        }

        // Economy changes
        if (isEconomyChangesEnabled) {
            pluginManager.registerEvents(EconomyChangesListener(), this)
            Log.withPrefix("EconomyChangeListener je aktivován.")
        }
        if (isLevelsChangesEnabled) {
            pluginManager.registerEvents(EconomyLevelUpListener(), this)
            Log.withPrefix("EconomyLevelUpListener je aktivován.")
        }
        if (isCommandLoggerEnabled) {
            pluginManager.registerEvents(CommandLogListener(), this)
            Log.withPrefix("CommandLogListener je aktivován.")
        }
        if (isChatLoggerEnabled) {
            pluginManager.registerEvents(ChatLoggerListener(), this)
            Log.withPrefix("ChatLoggerListener je aktivován.")
            if (this.server.pluginManager.isPluginEnabled("PlotSquared")) {
                pluginManager.registerEvents(PlotChatListener(), this)
                Log.withPrefix("PlotChatListener je aktivován.")
            }
        }
        if (isJoinAndLeaveEnabled) {
            pluginManager.registerEvents(JoinAndLeaveLoggerListener(), this)
            Log.withPrefix("JoinAndLeaveLoggerListener je aktivován.")
        }

        if (isVotePartyLoggedEnabled && this.server.pluginManager.isPluginEnabled("VoteParty")) {
            Log.withPrefix("VotePartyListener je aktivován.")
            pluginManager.registerEvents(VotePartyListener(), this)
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