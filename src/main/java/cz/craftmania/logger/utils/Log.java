package cz.craftmania.logger.utils;

import cz.craftmania.logger.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Log {

    public static void withPrefix(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[CraftLogger] " + ChatColor.WHITE + s);
    }

    public static void normalMessage(String s) {
        Bukkit.getConsoleSender().sendMessage(s);
    }

    public static void debug(String s) {
        if (Main.getInstance().isDebugEnabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[CraftLogger - DEBUG] " + ChatColor.WHITE + s);
        }
    }

}
