package cz.craftmania.logger.listeners;

import cz.craftmania.crafteconomy.events.PlayerLevelUpEvent;
import cz.craftmania.logger.Main;
import cz.craftmania.logger.utils.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EconomyLevelUpListener implements Listener {

    @EventHandler
    public void onPlayerLevelUp(final PlayerLevelUpEvent event) {
        Player player = event.getPlayer().getPlayer(); // ?????
        Main.getInstance().getSQL().createDataLog(player, "levels", "LEVEL_UP", event.getNewLevel(), System.currentTimeMillis());
        Log.debug("[A:LEVEL_UP]: " + player.getName() + "(" + player.getUniqueId().toString() + ") -> " + event.getAmount());
    }
}
