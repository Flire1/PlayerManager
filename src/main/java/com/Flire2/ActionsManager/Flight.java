package com.Flire2.ActionsManager;

import com.Flire2.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.UUID;

public class Flight implements Listener {

    private static final HashSet<UUID> flightPlayers = new HashSet<>();

    public static void updateFlightStatus(Player player) {
        Object flightObj = PlayerManager.getInstance().getPlayerDataManagerTemp().get(player.getUniqueId(), "allowFlight");
        boolean allowFlight = flightObj != null && flightObj.equals(true);

        if (allowFlight) {
            flightPlayers.add(player.getUniqueId());
            player.setAllowFlight(true);
        } else {
            flightPlayers.remove(player.getUniqueId());
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updateFlightStatus(player);
    }
}
