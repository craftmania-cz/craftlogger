package cz.craftmania.logger.utils

import cz.craftmania.logger.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object Log {

    fun withPrefix(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN.toString() + "[CraftLogger] " + ChatColor.WHITE + s)
    }

    fun error(s: String) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED.toString() + "[CraftLogger - ERROR] " + ChatColor.WHITE + s)
    }

    fun normalMessage(s: String?) {
        Bukkit.getConsoleSender().sendMessage(s!!)
    }

    fun debug(s: String) {
        if (Main.instance!!.isDebugEnabled) {
            Bukkit.getConsoleSender()
                .sendMessage(ChatColor.BLUE.toString() + "[CraftLogger - DEBUG] " + ChatColor.WHITE + s)
        }
    }
}