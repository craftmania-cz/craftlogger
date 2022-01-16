package cz.craftmania.logger.commands

import co.aikar.commands.ACFBukkitUtil.sendMsg
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.*
import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.command.CommandSender

@CommandAlias("logger")
@Description("Hlavní příkaz na ovládání CraftLoggeru.")
class LoggerCommand : BaseCommand() {

    @HelpCommand
    fun helpCommand(sender: CommandSender, help: CommandHelp) {
        sendMsg(sender, "§e§lLogger commands:")
        help.showHelp()
    }

    @Default
    fun defaultCommand(sender: CommandSender) {
        sender.sendMessage("§eCraftLogger je aktivní!")
    }

    @Subcommand("manualsave")
    @CommandPermission("craftlogger.admin.manualsave")
    @CommandCompletion("[typ]")
    @Syntax("[typ]")
    fun manualSave(sender: CommandSender, typ: String) {
        when (typ) {
            "pinata" -> {
                Main.instance!!.sQL!!.createDataLog(
                    "PINATA", "vote_events", "START", null, System.currentTimeMillis())
                Log.withPrefix("[A:PINATA]: Action called.")
                sender.sendMessage("§eStart pinaty byl uložen do SQL.")
            } else -> {
                sender.sendMessage("§cZadaný typ ukládání není podporovaný nebo neexistuje.")
            }
        }
    }
}