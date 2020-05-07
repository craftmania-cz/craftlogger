package cz.craftmania.logger.listeners;

import cz.craftmania.crafteconomy.events.vault.CraftEconomyPlayerPayEvent;
import cz.craftmania.crafteconomy.events.vault.PlayerVaultDepositEvent;
import cz.craftmania.crafteconomy.events.vault.PlayerVaultWithdrawEvent;
import cz.craftmania.logger.Main;
import cz.craftmania.logger.utils.Log;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EconomyChangesListener implements Listener {

    /**
     * Pokud hráč obdrží nějakou částku!
     * @param event
     */
    @EventHandler
    public void onPlayerDepositMoney(final PlayerVaultDepositEvent event) {
        OfflinePlayer player = event.getOfflinePlayer();
        Main.getInstance().getSQL().createPlayerEconomyLog(player, "MONEY_DEPOSIT", (long) event.getAmount(), System.currentTimeMillis());
        Log.withPrefix("[A:MONEY_DEPOSIT]: " + player.getName() + "(" + player.getUniqueId().toString() + ") -> " + event.getAmount());
    }

    /**
     * Pokud je hráči částka odebrána!
     * @param event
     */
    @EventHandler
    public void onPlayerWithdrawMoney(final PlayerVaultWithdrawEvent event) {
        OfflinePlayer player = event.getOfflinePlayer();
        Main.getInstance().getSQL().createPlayerEconomyLog(player, "MONEY_WITHDRAW", (long) event.getAmount(), System.currentTimeMillis());
        Log.withPrefix("[A:MONEY_WITHDRAW]: " + player.getName() + "(" + player.getUniqueId().toString() + ") -> " + event.getAmount());
    }

    /**
     * Pokud hráč provede /pay [nick] [částka]
     * @param event
     */
    @EventHandler
    public void onPlayerPayMoney(final CraftEconomyPlayerPayEvent event) {
        Player reciever =  event.getReciever();
        Player sender = event.getSender();
        Main.getInstance().getSQL().createPlayerEconomyLog(sender, reciever, "PAY_COMMAND", event.getAmount(), System.currentTimeMillis());
        Log.withPrefix("[A:PAY_COMMAND]: " + sender.getName() + "(" + sender.getUniqueId().toString() + ") =>> " + reciever.getName() + "(" + reciever.getUniqueId().toString() + ") - " + event.getAmount());
    }
}

