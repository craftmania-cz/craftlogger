package cz.craftmania.logger.listeners.external

import cz.craftmania.craftactions.economy.AsyncPlayerLevelUpEvent
import cz.craftmania.logger.Main
import cz.craftmania.logger.objects.LogType
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EconomyLevelUpListener : Listener {

    @EventHandler
    fun onPlayerLevelUp(event: AsyncPlayerLevelUpEvent) {
        val player = event.player.player!! // ?????
        Main.instance!!.sQL!!.createDataLog(player, LogType.LEVELS, "LEVEL_UP", event.newLevel.toLong(), System.currentTimeMillis())
        Log.debug("[A:LEVEL_UP]: " + player.name + "(" + player.uniqueId.toString() + ") -> " + event.newLevel)
    }
}