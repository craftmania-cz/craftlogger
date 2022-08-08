package cz.craftmania.logger.listeners.external

import cz.craftmania.logger.Main
import cz.craftmania.logger.objects.LogType
import cz.craftmania.logger.utils.Log
import cz.craftmania.logger.utils.LogUtils
import me.angeschossen.lands.api.events.LandChatEvent
import me.angeschossen.lands.api.events.LandCreateEvent
import me.angeschossen.lands.api.events.LandDeleteEvent
import me.angeschossen.lands.api.events.LandRenameEvent
import me.angeschossen.lands.api.events.memberholder.MemberHolderUpkeepEvent
import me.angeschossen.lands.api.events.nation.NationCreateEvent
import me.angeschossen.lands.api.events.nation.NationDeleteEvent
import me.angeschossen.lands.api.events.player.PlayerTaxEvent
import me.angeschossen.lands.api.events.war.WarDeclareEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.json.simple.JSONObject

class LandsListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerLandsChat(event: LandChatEvent) {
        val player: Player? = event.sender

        if (player != null) {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                player.name,
                "LANDS_CHAT",
                event.message,
                System.currentTimeMillis()
            )
        } else {
            Log.error("Event LandsChatEvent failed - player is null");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun landsRenameEvent(event: LandRenameEvent) {
        val jsonObject = JSONObject()
        jsonObject["old"] = event.currentName
        jsonObject["new"] = event.newName
        jsonObject["owner"] = event.land.ownerUID.toString()
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                event.currentName,
                "LANDS_RENAME",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event LandRenameEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun landsUpkeepPay(event: MemberHolderUpkeepEvent) {
        val jsonObject = JSONObject()
        jsonObject["value"] = event.upkeep
        jsonObject["balance"] = event.balance
        jsonObject["owner"] = event.memberHolder.ownerUID.toString()
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                event.memberHolder.name,
                "LANDS_UPKEEP_PAY",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event MemberHolderUpkeepEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun landsCreateEvent(event: LandCreateEvent) {
        val jsonObject = JSONObject()
        jsonObject["name"] = event.land.name
        jsonObject["owner"] = event.land.ownerUID.toString()
        if (event.land.spawn != null) {
            jsonObject["location"] = LogUtils.prepareJsonLocation(event.land.spawn!!)
        }
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                event.land.name,
                "LANDS_CREATE",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event LandCreateEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun landsDeleteEvent(event: LandDeleteEvent) {
        val jsonObject = JSONObject()
        jsonObject["reason"] = event.reason.name
        jsonObject["name"] = event.land.name
        jsonObject["owner"] = event.land.ownerUID.toString()
        jsonObject["balance"] = event.land.balance
        jsonObject["chunks_amount"] = event.land.chunksAmount
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                event.land.name,
                "LANDS_DELETE",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event LandDeleteEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerTayPayEvent(event: PlayerTaxEvent) {
        val jsonObject = JSONObject()
        jsonObject["area"] = event.area.land.name
        jsonObject["tax"] = event.area.tax
        jsonObject["balance"] = event.balance
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.LANDS,
                event.player.toString(),
                "LANDS_TAX_PAY",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event PlayerTaxEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun nationCreateEvent(event: NationCreateEvent) {
        val jsonObject = JSONObject()
        jsonObject["owner"] = event.nation.ownerUID
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.NATIONS,
                event.nation.name,
                "NATION_CREATE",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event NationCreateEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun nationDeleteEvent(event: NationDeleteEvent) {
        val jsonObject = JSONObject()
        jsonObject["owner"] = event.nation.ownerUID
        jsonObject["reason"] = event.reason.name
        jsonObject["balance"] = event.nation.balance
        jsonObject["lands_amount"] = event.nation.lands.size
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.NATIONS,
                event.nation.name,
                "NATION_DELETE",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event NationDeleteEvent failed");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun nationDeleteEvent(event: WarDeclareEvent) {
        val jsonObject = JSONObject()
        jsonObject["attacker"] = event.attacker.name
        jsonObject["defender"] = event.defender.name
        try {
            Main.instance!!.sQL!!.createDataLog(
                LogType.WARS,
                event.sender.player.name,
                "WARS_DECLARE",
                jsonObject.toString(),
                System.currentTimeMillis()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            Log.error("Event NationDeleteEvent failed");
        }
    }
}