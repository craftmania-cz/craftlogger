package cz.craftmania.logger.listeners

import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandLogListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        val split = event.message.split(" ").toTypedArray()
        val length = split.size
        var i = 0
        while (i < length) {
            val word = split[i]
            if (Main.instance!!.config.getStringList("logger.commands.ignored").contains(word)) {
                return
            }
            ++i
        }
        Main.instance!!.sQL!!.createDataLog(
            event.player,
            "command",
            "COMMAND",
            event.message,
            System.currentTimeMillis()
        )
        Log.debug("[A:COMMAND]: " + event.player.name + "(" + event.player.uniqueId.toString() + ") -> " + event.message)
    }
}