package com.Flire2;

import com.Flire2.Commands.PlayerManagerCommand;
import com.Flire2.GUI.*;
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
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new EditStatsGUI(), this);

        getServer().getPluginManager().registerEvents(new BanGUI(), this);
        getServer().getPluginManager().registerEvents(new BanGUI.BanGUIDuration(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
