package cz.craftmania.logger.listeners

import cz.craftmania.crafteconomy.events.PlayerLevelUpEvent
import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EconomyLevelUpListener : Listener {

    @EventHandler
    fun onPlayerLevelUp(event: PlayerLevelUpEvent) {
        val player = event.player.player // ?????
        Main.instance!!.sQL!!.createDataLog(player, "levels", "LEVEL_UP", event.newLevel, System.currentTimeMillis())
        Log.debug("[A:LEVEL_UP]: " + player.name + "(" + player.uniqueId.toString() + ") -> " + event.amount)
    }
}