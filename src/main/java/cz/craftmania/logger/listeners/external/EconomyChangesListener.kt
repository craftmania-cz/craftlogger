package cz.craftmania.logger.listeners.external

import cz.craftmania.crafteconomy.events.vault.CraftEconomyPlayerPayEvent
import cz.craftmania.crafteconomy.events.vault.PlayerVaultDepositEvent
import cz.craftmania.crafteconomy.events.vault.PlayerVaultWithdrawEvent
import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EconomyChangesListener : Listener {

    /**
     * Pokud hráč obdrží nějakou částku!
     * @param event
     */
    @EventHandler
    fun onPlayerDepositMoney(event: PlayerVaultDepositEvent) {
        val player = event.offlinePlayer
        Main.instance!!.sQL!!.createPlayerEconomyLog(
            player, "MONEY_DEPOSIT", event.amount
                .toLong(), System.currentTimeMillis()
        )
        Log.withPrefix("[A:MONEY_DEPOSIT]: " + player.name + "(" + player.uniqueId.toString() + ") -> " + event.amount)
    }

    /**
     * Pokud je hráči částka odebrána!
     * @param event
     */
    @EventHandler
    fun onPlayerWithdrawMoney(event: PlayerVaultWithdrawEvent) {
        val player = event.offlinePlayer
        Main.instance!!.sQL!!.createPlayerEconomyLog(
            player, "MONEY_WITHDRAW", event.amount
                .toLong(), System.currentTimeMillis()
        )
        Log.withPrefix("[A:MONEY_WITHDRAW]: " + player.name + "(" + player.uniqueId.toString() + ") -> " + event.amount)
    }

    /**
     * Pokud hráč provede /pay [nick] [částka]
     * @param event
     */
    @EventHandler
    fun onPlayerPayMoney(event: CraftEconomyPlayerPayEvent) {
        val reciever = event.reciever
        val sender = event.sender
        Main.instance!!.sQL!!.createPlayerEconomyLog(
            sender,
            reciever,
            "PAY_COMMAND",
            event.amount,
            System.currentTimeMillis()
        )
        Log.withPrefix("[A:PAY_COMMAND]: " + sender.name + "(" + sender.uniqueId.toString() + ") =>> " + reciever.name + "(" + reciever.uniqueId.toString() + ") - " + event.amount)
    }
}