package cz.craftmania.logger;

import cz.craftmania.logger.listeners.LuckPermsListener;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    LuckPerms luckPermsApi;

    @Override
    public void onEnable() {

        // Instance
        instance = this;

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();
        }

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new LuckPermsListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public LuckPerms getLuckPermsApi() {
        return luckPermsApi;
    }
}
