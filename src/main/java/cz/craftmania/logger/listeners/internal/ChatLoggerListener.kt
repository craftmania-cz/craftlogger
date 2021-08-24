package cz.craftmania.logger.listeners.internal

import cz.craftmania.logger.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatLoggerListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        Main.instance!!.sQL!!.createDataLog(event.player, "chat", "MESSAGE", event.message, System.currentTimeMillis())
    }
}