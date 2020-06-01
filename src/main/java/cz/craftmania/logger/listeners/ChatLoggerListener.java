package cz.craftmania.logger.listeners;

import cz.craftmania.logger.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLoggerListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event){
        Main.getInstance().getSQL().createDataLog(event.getPlayer(), "chat", "MESSAGE", event.getMessage(), System.currentTimeMillis());
    }
}
