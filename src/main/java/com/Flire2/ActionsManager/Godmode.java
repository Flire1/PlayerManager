package com.Flire2.ActionsManager;

import com.Flire2.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.UUID;

public class Godmode implements Listener {

    private static final HashSet<UUID> godmodePlayers = new HashSet<>();

    public static void updateGodmodeStatus(Player player) {
        Object godmodeObj = PlayerManager.getInstance().getPlayerDataManagerTemp().get(player.getUniqueId(), "hasGodmode");
        boolean hasGodmode = godmodeObj != null && godmodeObj.equals(true);

        if (hasGodmode) {
            godmodePlayers.add(player.getUniqueId());
        } else {
            godmodePlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateGodmodeStatus(player);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (godmodePlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
