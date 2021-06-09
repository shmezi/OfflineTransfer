package me.alexiriving992.offtransfer.main;

import ml.karmaconfigs.api.common.utils.StringUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin implements Listener {

    Map<String, String> players = new HashMap<String, String>();
    FileConfiguration config = this.getConfig();
    Plugin vaultplugin = Bukkit.getPluginManager().getPlugin("PlayerVaults");
    LuckPerms luckPermsApi;


    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        Object map = StringUtils.load(config.get("MAP").toString());
        Map<String, String> mapper = (HashMap<String, String>) map;
        players.clear();
        players.putAll(mapper);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPermsApi = provider.getProvider();

        }


    }


    @Override
    public void onDisable() {

        // Plugin shutdown logic
        String serialized = StringUtils.serialize(players);

        config.set("MAP", serialized);
        saveConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playern = player.getName();
        Player oldPlayer = Bukkit.getPlayer(players.get(MojangAuth.main(playern)));
        if (players.get(MojangAuth.main(playern)) != null) {

            if (players.get(MojangAuth.main(playern)).equals(playern)) {
                System.out.println("User not changed.");
            } else {
                int i = 0;
                int i2 = 0;
                System.out.println("User switched from " + players.get(MojangAuth.main(playern)) + " to " + playern);
                for (ItemStack loopvalue : oldPlayer.getInventory()) {
                    player.getInventory().setItem(i, loopvalue);
                    i++;
                }
                for (ItemStack loopvalue : oldPlayer.getEnderChest()) {
                    player.getEnderChest().setItem(i2, loopvalue);
                    i2++;
                }
                player.setExp(oldPlayer.getExp());

            }

        } else {
            players.put(MojangAuth.main(playern), playern);

        }
    }
}
