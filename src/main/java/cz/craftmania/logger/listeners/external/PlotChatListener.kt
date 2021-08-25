package cz.craftmania.logger.listeners.external

import com.plotsquared.bukkit.player.BukkitPlayer
import com.plotsquared.bukkit.util.BukkitUtil
import com.plotsquared.core.location.Location
import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent


class PlotChatListener: Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlotChat(event: AsyncPlayerChatEvent) {
        val player: Player = event.player
        val bukkitPlayer: BukkitPlayer = BukkitUtil.adapt(player)
        val location: Location = bukkitPlayer.location
        val area = location.plotArea ?: return
        val plot = area.getPlot(location) ?: return

        if (!(area.isPlotChat && bukkitPlayer.getAttribute("chat")) || area.isForcingPlotChat) {
            return
        }

        Main.instance!!.sQL!!.createDataLog(
            event.player,
            "chat",
            "PLOT_MESSAGE",
            event.message,
            System.currentTimeMillis()
        )
        Log.debug("[A:PLOT_CHAT]: ${player.name} - ${plot.id} -> ${event.message}")
    }
}