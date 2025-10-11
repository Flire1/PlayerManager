package com.Flire2;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManagerTemp {

    private static File playersFolder;

    public PlayerDataManagerTemp(JavaPlugin plugin) {
        File pluginFolder = plugin.getDataFolder();
        playersFolder = new File(pluginFolder, "players");

        if (!playersFolder.exists()) {
            boolean created = playersFolder.mkdirs();
            Bukkit.getLogger().info("Created players folder: " + created);
        }

        clearTempFiles();
    }

    private static File getPlayerFolder(UUID uuid) {
        return new File(playersFolder, uuid.toString());
    }

    private static File getPlayerTempFile(UUID uuid) {
        return new File(getPlayerFolder(uuid), "temp.yml");
    }

    public static void set(UUID uuid, String key, Object value) {
        File file = getPlayerTempFile(uuid);
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
        File file = getPlayerTempFile(uuid);
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.get(key);
    }

    private void clearTempFiles() {
        if (playersFolder.exists()) {
            for (File uuidFolder : playersFolder.listFiles()) {
                if (uuidFolder.isDirectory()) {
                    File tempFile = new File(uuidFolder, "temp.yml");
                    if (tempFile.exists()) {
                        boolean deleted = tempFile.delete();
                        Bukkit.getLogger().info("Deleted temp file: " + tempFile.getPath() + " - Success: " + deleted);
                    }
                }
            }
        }
    }
}
