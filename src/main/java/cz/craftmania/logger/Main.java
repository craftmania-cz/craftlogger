package cz.craftmania.logger;

import cz.craftmania.logger.listeners.EconomyChangesListener;
import cz.craftmania.logger.listeners.EconomyLevelUpListener;
import cz.craftmania.logger.listeners.LuckPermsListener;
import cz.craftmania.logger.sql.SQLManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private boolean debugEnabled = false;
    private String serverId = "test";
    private LuckPerms luckPermsApi;
    private SQLManager sql;

    private boolean vipChangesEnabled = false;
    private boolean economyChangesEnabled = false;
    private boolean levelsChangesEnabled = false;

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        //Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        debugEnabled = getConfig().getBoolean("debug", false);
        serverId = getConfig().getString("server", "test");

        vipChangesEnabled = getConfig().getBoolean("logger.vip-status", false);
        economyChangesEnabled = getConfig().getBoolean("logger.economy-changes", false);
        levelsChangesEnabled = getConfig().getBoolean("logger.levels-change", false);

        // HikariCP
        sql = new SQLManager(this);

        // Plugin loader
        PluginManager pluginManager = this.getServer().getPluginManager();

        // VIP changes setting
        if (vipChangesEnabled) {

            // LuckPerms register
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                luckPermsApi = provider.getProvider();
            }

            pluginManager.registerEvents(new LuckPermsListener(), this);
        }

        // Economy changes
        if (economyChangesEnabled) {
            pluginManager.registerEvents(new EconomyChangesListener(), this);
        }

        if (levelsChangesEnabled) {
            pluginManager.registerEvents(new EconomyLevelUpListener(), this);
        }
    }

    @Override
    public void onDisable() {

        // Deaktivace MySQL
        sql.onDisable();

        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public SQLManager getSQL() {
        return sql;
    }

    public String getServerId() {
        return serverId;
    }

    public boolean isVipChangesEnabled() {
        return vipChangesEnabled;
    }

    public boolean isEconomyChangesEnabled() {
        return economyChangesEnabled;
    }

    public boolean isLevelsChangesEnabled() {
        return levelsChangesEnabled;
    }
}
