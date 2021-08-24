package cz.craftmania.logger.listeners.external

import cz.craftmania.logger.Main
import cz.craftmania.logger.utils.Log
import net.luckperms.api.context.Context
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.function.Consumer

class LuckPermsListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(Main.instance!!, Runnable {
            if (Main.instance!!.sQL!!.getLastUpdateVIP(player.uniqueId) > System.currentTimeMillis() - 1800000L) { // 30 minut
                Log.debug("Hrac nedosahl data updatu VIP statusu.")
                return@Runnable
            }

            val user: User?
            try {
                user = Main.instance!!.luckPermsApi!!.userManager.loadUser(player.uniqueId).get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
                return@Runnable
            } catch (e: ExecutionException) {
                e.printStackTrace()
                return@Runnable
            }
            val finalJson = JSONObject()

            when (user!!.primaryGroup) {
                "owner" -> finalJson["primary"] = "owner"
                "manager" -> finalJson["primary"] = "manager"
                "developer" -> finalJson["primary"] = "developer"
                "eventer" -> finalJson["primary"] = "eventer"
                "adminka" -> finalJson["primary"] = "adminka"
                "admin" -> finalJson["primary"] = "admin"
                "builder" -> finalJson["primary"] = "builder"
                "artist" -> finalJson["primary"] = "artist"
                "helperka" -> finalJson["primary"] = "helperka"
                "helper" -> finalJson["primary"] = "helper"
                "tester" -> finalJson["primary"] = "tester"
                else -> {
                    val globalVipRanks = arrayOf("obsidian", "emerald", "diamond", "gold")
                    user.distinctNodes.forEach(Consumer { node: Node ->
                        run {
                            for (vipType in globalVipRanks) {
                                if (node.key.contains("group.$vipType")) {
                                    val contexts = node.contexts
                                    if (contexts.size() == 0) {
                                        finalJson["primary"] = vipType
                                        try {
                                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                                        } catch (e: NullPointerException) {
                                            finalJson["time"] = 0
                                        }
                                        break
                                    }
                                }
                            }
                        }
                    })
                }
            }

            // Prepare servers array
            val servers = JSONObject()
            finalJson["servers"] = servers

            // VIP ranky
            val vipArray = arrayOf("obsidian", "emerald", "diamond", "gold")
            user.distinctNodes.forEach(Consumer { node: Node ->
                for (vipType in vipArray) {
                    if (node.key.contains("group.$vipType")) {
                        val contexts = node.contexts
                        contexts.forEach(Consumer { data: Context ->
                            if (data.key.contains("server")) {
                                val vipData = JSONArray()
                                val vip = JSONObject()
                                vip["group"] = vipType
                                try {
                                    vip["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                                } catch (e: NullPointerException) {
                                    vip["time"] = 0
                                }
                                vipData.add(vip)
                                if (servers.containsKey(data.value)) {
                                    val existsArray = servers[data.value] as JSONArray
                                    existsArray.add(vip)
                                } else {
                                    servers[data.value] = vipData
                                }
                            }
                        })
                    }
                }
            })
            Log.debug(finalJson.toJSONString())
            Main.instance!!.sQL!!.updateVIP(player.uniqueId, finalJson)
            Main.instance!!.sQL!!.updateLastUpdateVIP(player.uniqueId, System.currentTimeMillis())
        }, 10L)
    }
}