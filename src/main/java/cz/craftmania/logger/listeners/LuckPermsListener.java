package cz.craftmania.logger.listeners;

import cz.craftmania.logger.Main;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class LuckPermsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        User user = Main.getInstance().getLuckPermsApi().getUserManager().loadUser(player.getUniqueId()).get();
        System.out.println("Primary: " + user.getPrimaryGroup());
        System.out.println("Nodes: " + Arrays.toString(user.getNodes().toArray()));
        System.out.println("------");

        JSONObject finalJson = new JSONObject();

        // Detekce primarní skupiny (global VIP)
        user.getNodes().forEach(node -> {
            if (node.getKey().contains("group.obsidian")) {
                ImmutableContextSet contexts = node.getContexts();
                if (contexts.size() == 0) {
                    finalJson.put("primary", "obsidian"); //TODO: Time + AT
                    return;
                }
            }
            if (node.getKey().contains("group.emerald")) {
                ImmutableContextSet contexts = node.getContexts();
                if (contexts.size() == 0) {
                    finalJson.put("primary", "emerald");
                    return;
                }
            }
            if (node.getKey().contains("group.diamond")) {
                ImmutableContextSet contexts = node.getContexts();
                if (contexts.size() == 0) {
                    finalJson.put("primary", "diamond");
                    return;
                }
            }
            if (node.getKey().contains("group.gold")) {
                ImmutableContextSet contexts = node.getContexts();
                if (contexts.size() == 0) {
                    finalJson.put("primary", "gold");
                    return;
                }
            }
        });

        System.out.println(finalJson.toJSONString());

        // Prepare servers array
        JSONObject servers = new JSONObject();
        finalJson.put("servers", servers);

        System.out.println(finalJson.toJSONString());

        user.getNodes().forEach(node -> {
            if (node.getKey().contains("group.gold")) { //TODO: All vip ranky
                ImmutableContextSet contexts = node.getContexts();
                contexts.forEach(data -> {
                    if (data.getKey().contains("server")) {
                        System.out.println("Server: " + data.getValue());
                        JSONArray vipData = new JSONArray();
                        JSONObject vip = new JSONObject();
                        vip.put("group", "gold");
                        vip.put("time", node.getExpiry());
                        vipData.add(vip);
                        if (servers.containsKey(data.getValue())) {
                            JSONArray existsArray = (JSONArray) servers.get(data.getValue());
                            existsArray.add(vip);
                        } else {
                            servers.put(data.getValue(), vipData);
                        }
                    }
                });
            }
        });

        System.out.println(finalJson.toJSONString());
    }
}
