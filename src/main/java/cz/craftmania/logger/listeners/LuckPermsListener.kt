package cz.craftmania.logger.listeners

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
            if (Main.instance!!.sQL!!.getLastUpdateVIP(player.uniqueId) > System.currentTimeMillis() - 10L) { // 1 den
                Log.debug("Hrac nedosahl data updatu VIP statusu.")
                return@Runnable
            }
            var user: User? = null
            try {
                user = Main.instance!!.luckPermsApi!!.userManager.loadUser(player.uniqueId).get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
            val finalJson = JSONObject()

            // Detekce primarní skupiny (global VIP nebo skupiny)
            user!!.nodes.forEach(Consumer { node: Node ->
                if (node.key.contains("group.owner")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "owner"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.manager")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "manager"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.developer")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "developer"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.eventer")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "eventer"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.adminka")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "adminka"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.admin")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "admin"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.builder")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "builder"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.artist")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "artist"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.helperka")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "helperka"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.helper")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "helper"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.tester")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "tester"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.obsidian")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "obsidian"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.emerald")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "emerald"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.diamond")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "diamond"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
                if (node.key.contains("group.gold")) {
                    val contexts = node.contexts
                    if (contexts.size() == 0) {
                        finalJson["primary"] = "gold"
                        try {
                            finalJson["time"] = Objects.requireNonNull(node.expiry)!!.toEpochMilli()
                        } catch (e: NullPointerException) {
                            finalJson["time"] = 0
                        }
                        return@Consumer
                    }
                }
            })

            // Prepare servers array
            val servers = JSONObject()
            finalJson["servers"] = servers

            // VIP ranky
            val vipArray = arrayOf("obsidian", "emerald", "diamond", "gold")
            user.nodes.forEach(Consumer { node: Node ->
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