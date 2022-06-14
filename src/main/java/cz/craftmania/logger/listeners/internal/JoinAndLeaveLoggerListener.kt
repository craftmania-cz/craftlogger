package cz.craftmania.logger.listeners.internal

import cz.craftmania.logger.Main
import cz.craftmania.logger.objects.LogType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinAndLeaveLoggerListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        Main.instance!!.sQL!!.createDataLog(
            player,
            LogType.GATE,
            "JOIN",
            player.address?.address?.hostAddress,
            System.currentTimeMillis()
        )
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val player = event.player
        Main.instance!!.sQL!!.createDataLog(
            player,
            LogType.GATE,
            "LEAVE",
            player.address?.address?.hostAddress,
            System.currentTimeMillis()
        )
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        val player = event.player
        Main.instance!!.sQL!!.createDataLog(player, LogType.GATE, "KICK", null, System.currentTimeMillis())
    }
}