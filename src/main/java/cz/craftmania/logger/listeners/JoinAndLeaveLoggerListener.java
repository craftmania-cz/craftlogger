package cz.craftmania.logger.listeners;

import cz.craftmania.logger.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinAndLeaveLoggerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Main.getInstance().getSQL().createDataLog(player, "gate", "JOIN", player.getAddress().toString(), System.currentTimeMillis());
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        Main.getInstance().getSQL().createDataLog(player, "gate", "LEAVE", player.getAddress().toString(), System.currentTimeMillis());
    }

    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        final Player player = event.getPlayer();
        Main.getInstance().getSQL().createDataLog(player, "gate", "KICK", null, System.currentTimeMillis());
    }
}
