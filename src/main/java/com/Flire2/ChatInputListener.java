package com.Flire2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatInputListener implements Listener {

    public static final Map<UUID, Consumer<String>> inputMap = new HashMap<>();

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (inputMap.containsKey(uuid)) {
            event.setCancelled(true);

            String message = event.getMessage();
            Consumer<String> callback = inputMap.remove(uuid);

            Bukkit.getScheduler().runTask(PlayerManager.getInstance(), () -> {
                callback.accept(message);
            });
        }
    }
}
