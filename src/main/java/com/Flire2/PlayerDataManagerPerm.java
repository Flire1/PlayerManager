package com.Flire2;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManagerPerm {

    private static File playersFolder;

    public PlayerDataManagerPerm(JavaPlugin plugin) {
        File pluginFolder = plugin.getDataFolder();
        File playerManagerFolder = new File(pluginFolder, "PlayerManager");
        playersFolder = new File(playerManagerFolder, "players");

        if (!playersFolder.exists()) {
            boolean created = playersFolder.mkdirs();
            Bukkit.getLogger().info("Created players folder: " + created);
        }
    }

    private static File getPlayerFolder(UUID uuid) {
        return new File(playersFolder, uuid.toString());
    }

    private static File getPlayerPermFile(UUID uuid) {
        return new File(getPlayerFolder(uuid), "perm.yml");
    }

    public static void set(UUID uuid, String key, Object value) {
        File file = getPlayerPermFile(uuid);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(key, value);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object get(UUID uuid, String key) {
        File file = getPlayerPermFile(uuid);
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.get(key);
    }
}
