package cz.craftmania.logger.listeners.external

import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import me.clip.voteparty.events.PartyStartEvent

class VotePartyListener: Listener {

    @EventHandler
    fun onVotePartyEnd(event: PartyStartEvent) {
        Main.instance!!.sQL!!.createDataLog(
            "VOTEPARTY", "VOTE_PARTY", "START", null, System.currentTimeMillis())
        Log.withPrefix("[A:VOTEPARTY_START]: Action called.")
    }
}