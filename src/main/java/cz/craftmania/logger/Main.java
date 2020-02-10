package cz.craftmania.logger;

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
    private LuckPerms luckPermsApi;
    private SQLManager sql;

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        //Config
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        debugEnabled = getConfig().getBoolean("debug", false);

        // LuckPerms register
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();
        }

        // HikariCP
        sql = new SQLManager(this);

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new LuckPermsListener(), this);
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
}
