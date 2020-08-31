package cz.craftmania.logger.listeners;

import cz.craftmania.logger.Main;
import cz.craftmania.logger.utils.Log;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class LuckPermsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

            if (!(Main.getInstance().getSQL().getLastUpdateVIP(player.getUniqueId()) <= (System.currentTimeMillis() - 86400000L))) { // 1 den
                Log.debug("Hrac nedosahl data updatu VIP statusu.");
                return;
            }

            User user = null;
            try {
                user = Main.getInstance().getLuckPermsApi().getUserManager().loadUser(player.getUniqueId()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (user == null) return;

            JSONObject finalJson = new JSONObject();

            // Detekce primarní skupiny (global VIP nebo skupiny)
            user.getNodes().forEach(node -> {
                if (node.getKey().contains("group.owner")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "owner");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.developer")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "developer");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.eventer")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "eventer");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.admin")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "admin");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.builder")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "builder");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.helper")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "helper");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.tester")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "tester");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.obsidian")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "obsidian");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.emerald")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "emerald");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.diamond")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "diamond");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                        return;
                    }
                }
                if (node.getKey().contains("group.gold")) {
                    ImmutableContextSet contexts = node.getContexts();
                    if (contexts.size() == 0) {
                        addPrimaryGroupToObject(finalJson, "gold");
                        try {
                            finalJson.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                        } catch (NullPointerException e) {
                            finalJson.put("time", 0);
                        }
                    }
                }
            });

            // Prepare servers array
            JSONObject servers = new JSONObject();
            finalJson.put("servers", servers);

            // VIP ranky
            String[] vipArray = new String[]{"obsidian", "emerald", "diamond", "gold"};

            user.getNodes().forEach(node -> {
                for (String vipType : vipArray) {
                    if (node.getKey().contains("group." + vipType)) {
                        ImmutableContextSet contexts = node.getContexts();
                        contexts.forEach(data -> {
                            if (data.getKey().contains("server")) {
                                JSONArray vipData = new JSONArray();
                                JSONObject vip = new JSONObject();
                                vip.put("group", vipType);
                                try {
                                    vip.put("time", Objects.requireNonNull(node.getExpiry()).toEpochMilli());
                                } catch (NullPointerException e) {
                                    vip.put("time", 0);
                                }
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
                }
            });

            Log.debug(finalJson.toJSONString());

            Main.getInstance().getSQL().updateVIP(player.getUniqueId(), finalJson);
            Main.getInstance().getSQL().updateLastUpdateVIP(player.getUniqueId(), System.currentTimeMillis());

        }, 10L);
    }

    private JSONObject addPrimaryGroupToObject(JSONObject obj, String value) {
        GroupHierarchy groupHierarchy = GroupHierarchy.valueOf(value.toUpperCase());
        if (obj.containsKey("primary")) {
            GroupHierarchy current = GroupHierarchy.valueOf(obj.get("primary").toString());
            if (groupHierarchy.getValue() > current.getValue()) {
                obj.put("primary", value);
            }
        }
        return obj;
    }

    private enum GroupHierarchy {

        OWNER(12),
        MANAGER(11),
        DEVELOPER(10),
        EVENTER(9),
        ADMIN(8),
        BUILDER(7),
        HELPER(6),
        TESTER(5),
        OBSIDIAN(4),
        EMERALD(3),
        DIAMOND(2),
        GOLD(1),
        DEFAULT(0);

        private final int value;

        GroupHierarchy(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
