package cz.craftmania.logger.sql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import cz.craftmania.logger.Main
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class ConnectionPoolManager(private val plugin: Main) {

    private var dataSource: HikariDataSource? = null
    private var host: String? = null
    private var port: String? = null
    private var database: String? = null
    private var username: String? = null
    private var password: String? = null
    private var minimumConnections = 0
    private var maximumConnections = 0
    private var connectionTimeout: Long = 0

    init {
        init()
        setupPool()
    }

    private fun init() {
        host = plugin.config.getString("sql.hostname")
        port = plugin.config.getString("sql.port")
        database = plugin.config.getString("sql.database")
        username = plugin.config.getString("sql.username")
        password = plugin.config.getString("sql.password")
        minimumConnections = plugin.config.getInt("settings.minimumConnections")
        maximumConnections = plugin.config.getInt("settings.maximumConnections")
        connectionTimeout = plugin.config.getInt("settings.timeout").toLong()
    }

    private fun setupPool() {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://$host:$port/$database?characterEncoding=UTF-8&autoReconnect=true&useSSL=false"
        config.driverClassName = "com.mysql.jdbc.Driver"
        config.username = username
        config.password = password
        config.minimumIdle = minimumConnections
        config.maximumPoolSize = maximumConnections
        config.connectionTimeout = connectionTimeout
        config.poolName = "craftlogger"
        dataSource = HikariDataSource(config)
    }

    @get:Throws(SQLException::class)
    val connection: Connection
        get() = dataSource!!.connection

    fun closePool() {
        if (dataSource != null && !dataSource!!.isClosed) {
            dataSource!!.close()
        }
    }

    fun close(conn: Connection?, ps: PreparedStatement?, res: ResultSet?) {
        if (conn != null) try {
            conn.close()
        } catch (ignored: SQLException) {
        }
        if (ps != null) try {
            ps.close()
        } catch (ignored: SQLException) {
        }
        if (res != null) try {
            res.close()
        } catch (ignored: SQLException) {
        }
    }
}