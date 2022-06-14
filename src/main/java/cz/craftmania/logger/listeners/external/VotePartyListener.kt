package cz.craftmania.logger.listeners.external

import cz.craftmania.logger.Main
import cz.craftmania.logger.objects.LogType
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import me.clip.voteparty.events.PartyStartEvent
import java.util.UUID

class VotePartyListener: Listener {

    @EventHandler
    fun onVotePartyEnd(event: PartyStartEvent) {
        Main.instance!!.sQL!!.createDataLog(
            LogType.VOTE_EVENTS, UUID.randomUUID().toString(), "VOTEPARTY", null, System.currentTimeMillis())
        Log.withPrefix("[A:VOTEPARTY]: Action called.")
    }
}