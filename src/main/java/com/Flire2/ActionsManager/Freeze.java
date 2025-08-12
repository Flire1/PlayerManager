package com.Flire2.ActionsManager;

import com.Flire2.PlayerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.UUID;

public class Freeze implements Listener {

    private static final HashSet<UUID> frozenPlayers = new HashSet<>();

    public static void updateFreezeStatus(Player player) {
        Object frozenObj = PlayerManager.getInstance().getPlayerDataManagerTemp().get(player.getUniqueId(), "isFrozen");
        boolean isFrozen = frozenObj != null && frozenObj.equals(true);

        if (isFrozen) {
            frozenPlayers.add(player.getUniqueId());
        } else {
            frozenPlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateFreezeStatus(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player.getUniqueId())) {
            Location from = event.getFrom();
            Location to = event.getTo();

            if (to != null && (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()
                    || to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch())) {
                event.setTo(from);
            }
        }
    }
}
