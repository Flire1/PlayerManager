package com.Flire2;

import com.Flire2.Commands.PlayerManagerCommand;
import com.Flire2.GUI.EditStatsGUI;
import com.Flire2.GUI.KickGUI;
import com.Flire2.GUI.ModerationGUI;
import com.Flire2.GUI.PlayerManagerGUI;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerManager extends JavaPlugin {

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        getCommand("pm").setExecutor(new PlayerManagerCommand());
        getCommand("pm").setTabCompleter(new PlayerManagerCommand());

        getServer().getPluginManager().registerEvents(new PlayerManagerGUI(), this);
        getServer().getPluginManager().registerEvents(new ModerationGUI(), this);
        getServer().getPluginManager().registerEvents(new KickGUI(), this);
        getServer().getPluginManager().registerEvents(new com.Flire2.ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new EditStatsGUI(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
