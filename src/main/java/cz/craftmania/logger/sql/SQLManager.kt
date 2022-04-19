package cz.craftmania.logger.sql

import com.zaxxer.hikari.HikariDataSource
import cz.craftmania.logger.Main
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.json.simple.JSONObject
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*

class SQLManager(private val plugin: Main) {

    val pool: ConnectionPoolManager
    private val dataSource: HikariDataSource? = null
    fun onDisable() {
        pool.closePool()
    }

    fun getLastUpdateVIP(uuid: UUID): Long {
        var conn: Connection? = null
        var ps: PreparedStatement? = null
        try {
            conn = pool.connection
            ps = conn.prepareStatement("SELECT groups_last_check FROM minigames.player_profile WHERE uuid = '$uuid';")
            ps.executeQuery()
            if (ps.resultSet.next()) {
                return ps.resultSet.getLong("groups_last_check")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pool.close(conn, ps, null)
        }
        return 0
    }

    fun updateLastUpdateVIP(uuid: UUID, time: Long) {
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("UPDATE minigames.player_profile SET groups_last_check = '$time' WHERE uuid = ?;")
                    ps.setString(1, uuid.toString())
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun updateVIP(uuid: UUID, json: JSONObject) {
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("UPDATE minigames.player_profile SET groups = '" + json.toJSONString() + "' WHERE uuid = ?;")
                    ps.setString(1, uuid.toString())
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createPlayerEconomyLog(p: Player, action: String?, amount: Long, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,action,amount,time) VALUES (?,?,?,?,?);")
                    ps.setString(1, p.name)
                    ps.setString(2, p.uniqueId.toString())
                    ps.setString(3, action)
                    ps.setLong(4, amount)
                    ps.setLong(5, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createPlayerEconomyLog(p: OfflinePlayer, action: String?, amount: Long, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,action,amount,time) VALUES (?,?,?,?,?);")
                    ps.setString(1, p.name)
                    ps.setString(2, p.uniqueId.toString())
                    ps.setString(3, action)
                    ps.setLong(4, amount)
                    ps.setLong(5, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createPlayerEconomyLog(sender: Player, reciever: Player, action: String?, amount: Long, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,sender,s_uuid,action,amount,time) VALUES (?,?,?,?,?,?,?);")
                    ps.setString(1, sender.name)
                    ps.setString(2, sender.uniqueId.toString())
                    ps.setString(3, reciever.name)
                    ps.setString(4, reciever.uniqueId.toString())
                    ps.setString(5, action)
                    ps.setLong(6, amount)
                    ps.setLong(7, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createDataLog(p: Player, tableName: String, action: String?, data: String?, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs." + tableName + "_" + server + "_log (nick,uuid,action,data,time) VALUES (?,?,?,?,?);")
                    ps.setString(1, p.name)
                    ps.setString(2, p.uniqueId.toString())
                    ps.setString(3, action)
                    ps.setString(4, data)
                    ps.setLong(5, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createDataLog(p: Player, tableName: String, action: String?, data: Long, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs." + tableName + "_" + server + "_log (nick,uuid,action,data,time) VALUES (?,?,?,?,?);")
                    ps.setString(1, p.name)
                    ps.setString(2, p.uniqueId.toString())
                    ps.setString(3, action)
                    ps.setLong(4, data)
                    ps.setLong(5, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createDataLog(type: String, tableName: String, action: String?, data: String?, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs." + tableName + "_" + server + "_log (nick,uuid,action,data,time) VALUES (?,?,?,?,?);")
                    ps.setString(1, type)
                    ps.setString(3, action)
                    ps.setString(4, data)
                    ps.setLong(5, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    fun createDataLog(player: Player, tableName: String, deathType: String, location: String, killer: String?, inventory: String?, totemDeath: Boolean, time: Long) {
        val server: String = Main.instance!!.serverId
        object : BukkitRunnable() {
            override fun run() {
                var conn: Connection? = null
                var ps: PreparedStatement? = null
                try {
                    conn = pool.connection
                    ps =
                        conn.prepareStatement("INSERT INTO logs." + tableName + "_" + server + "_log (nick,uuid,deathType,location,killer,inventory,totemDeath,time) VALUES (?,?,?,?,?,?,?,?);")
                    ps.setString(1, player.name)
                    ps.setString(2, player.uniqueId.toString())
                    ps.setString(3, deathType)
                    ps.setString(4, location)
                    ps.setString(5, killer)
                    ps.setString(6, inventory)
                    ps.setBoolean(7, totemDeath)
                    ps.setLong(8, time)
                    ps.executeUpdate()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pool.close(conn, ps, null)
                }
            }
        }.runTaskAsynchronously(Main.instance!!)
    }

    init {
        pool = ConnectionPoolManager(plugin)
    }
}