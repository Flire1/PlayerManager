package com.Flire2;

import com.Flire2.Commands.PlayerManagerCommand;
import com.Flire2.GUI.*;
import com.Flire2.ActionsManager.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerManager extends JavaPlugin implements Listener{

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        new PlayerDataManagerPerm(this);
        playerDataManagerTemp = new PlayerDataManagerTemp(this);

        getCommand("pm").setExecutor(new PlayerManagerCommand());
        getCommand("pm").setTabCompleter(new PlayerManagerCommand());

        getServer().getPluginManager().registerEvents(new PlayerManagerGUI(), this);
        getServer().getPluginManager().registerEvents(new ModerationGUI(), this);
        getServer().getPluginManager().registerEvents(new KickGUI(), this);
        getServer().getPluginManager().registerEvents(new BanGUI(), this);
        getServer().getPluginManager().registerEvents(new BanGUI.BanGUIDuration(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new EditStatsGUI(), this);
        getServer().getPluginManager().registerEvents(new InventoryGUI(), this);
        getServer().getPluginManager().registerEvents(new InventoryGUI.PlayerInventory(), this);
        getServer().getPluginManager().registerEvents(new ActionsGUI(), this);
        getServer().getPluginManager().registerEvents(new Freeze(), this);
        getServer().getPluginManager().registerEvents(new Flight(), this);
        getServer().getPluginManager().registerEvents(new Godmode(), this);
        getServer().getPluginManager().registerEvents(new IdentityGUI(), this);
        getServer().getPluginManager().registerEvents(new BanIPGUI(), this);
        getServer().getPluginManager().registerEvents(new BanIPGUI.BanIPGUIDuration(), this);
        getServer().getPluginManager().registerEvents(new DisplayGUI(), this);
        getServer().getPluginManager().registerEvents(new SendTitleGUI(), this);
        getServer().getPluginManager().registerEvents(new SendActionBarGUI(), this);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Object displayName = PlayerDataManagerPerm.get(p.getUniqueId(), "displayName");
        if (displayName != null) {
            p.setDisplayName(displayName.toString());
        }

        Object tabListName = PlayerDataManagerPerm.get(p.getUniqueId(), "tabListName");
        if (tabListName != null) {
            p.setPlayerListName(tabListName.toString());
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private PlayerDataManagerTemp playerDataManagerTemp;

    public PlayerDataManagerTemp getPlayerDataManagerTemp() {
        return playerDataManagerTemp;
    }
}
