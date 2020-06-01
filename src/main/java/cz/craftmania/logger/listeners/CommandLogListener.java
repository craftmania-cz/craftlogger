package cz.craftmania.logger.listeners;

import cz.craftmania.logger.Main;
import cz.craftmania.logger.utils.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandLogListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommand(final PlayerCommandPreprocessEvent event){
        String[] split = event.getMessage().split(" ");
        for (int length = split.length, i = 0; i < length; ++i) {
            final String word = split[i];
            if (Main.getInstance().getConfig().getStringList("logger.commands.ignored").contains(word)) {
                return;
            }
        }
        Main.getInstance().getSQL().createCommandLog(event.getPlayer(), "COMMAND", event.getMessage(), System.currentTimeMillis());
        Log.debug("[A:COMMAND]: " + event.getPlayer().getName() + "(" + event.getPlayer().getUniqueId().toString() + ") -> " + event.getMessage());

    }
}
