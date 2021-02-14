package cz.craftmania.logger.sql;

import com.zaxxer.hikari.HikariDataSource;
import cz.craftmania.logger.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

public class SQLManager {

    private final Main plugin;
    private final ConnectionPoolManager pool;
    private HikariDataSource dataSource;

    public SQLManager(Main plugin) {
        this.plugin = plugin;
        pool = new ConnectionPoolManager(plugin);
    }

    public void onDisable() {
        pool.closePool();
    }

    public ConnectionPoolManager getPool() {
        return pool;
    }

    public final long getLastUpdateVIP(final UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT groups_last_check FROM minigames.player_profile WHERE uuid = '" + uuid.toString() + "';");
            ps.executeQuery();
            if (ps.getResultSet().next()) {
                return ps.getResultSet().getLong("groups_last_check");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, null);
        }
        return 0;
    }

    public final void updateLastUpdateVIP(UUID uuid, long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE minigames.player_profile SET groups_last_check = '" + time + "' WHERE uuid = ?;");
                    ps.setString(1, uuid.toString());
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void updateVIP(UUID uuid, JSONObject json) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("UPDATE minigames.player_profile SET groups = '" + json.toJSONString() + "' WHERE uuid = ?;");
                    ps.setString(1, uuid.toString());
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createPlayerEconomyLog(final Player p, final String action, final long amount, final long time) {
        String server = Main.getInstance().getServerId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,action,amount,time) VALUES (?,?,?,?,?);");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setString(3, action);
                    ps.setLong(4, amount);
                    ps.setLong(5, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createPlayerEconomyLog(final OfflinePlayer p, final String action, final long amount, final long time) {
        String server = Main.getInstance().getServerId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,action,amount,time) VALUES (?,?,?,?,?);");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setString(3, action);
                    ps.setLong(4, amount);
                    ps.setLong(5, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createPlayerEconomyLog(final Player sender, final Player reciever, final String action, final long amount, final long time) {
        String server = Main.getInstance().getServerId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO logs.economy_" + server + "_log (reciever,r_uuid,sender,s_uuid,action,amount,time) VALUES (?,?,?,?,?,?,?);");
                    ps.setString(1, sender.getName());
                    ps.setString(2, sender.getUniqueId().toString());
                    ps.setString(3, reciever.getName());
                    ps.setString(4, reciever.getUniqueId().toString());
                    ps.setString(5, action);
                    ps.setLong(6, amount);
                    ps.setLong(7, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createLevelUpLog(final Player p, final String action, final long level, final long time) {
        String server = Main.getInstance().getServerId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO logs.levels_" + server + "_log (nick,uuid,action,level,time) VALUES (?,?,?,?,?);");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setString(3, action);
                    ps.setLong(4, level);
                    ps.setLong(5, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public final void createDataLog(final Player p, final String tableName, final String action, final String data, final long time) {
        String server = Main.getInstance().getServerId();
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO logs." + tableName + "_" + server + "_log (nick,uuid,action,data,time) VALUES (?,?,?,?,?);");
                    ps.setString(1, p.getName());
                    ps.setString(2, p.getUniqueId().toString());
                    ps.setString(3, action);
                    ps.setString(4, data);
                    ps.setLong(5, time);
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pool.close(conn, ps, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }



}
