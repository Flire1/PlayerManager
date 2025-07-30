package com.Flire2;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PromptUtils {

    public static void doublePrompt(Player player, String promptMsg, Consumer<Double> callback) {
        player.sendMessage(ChatColor.YELLOW + promptMsg);

        ChatInputListener.inputMap.put(player.getUniqueId(), input -> {
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    player.sendMessage(ChatColor.RED + "Number must be positive.");
                    doublePrompt(player, promptMsg, callback);
                    return;
                }
                callback.accept(value);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid number. Please enter a valid number (e.g., 10 or 5.5).");
                doublePrompt(player, promptMsg, callback);
            }
        });
    }

    public static void intPrompt(Player player, String promptMsg, Consumer<Integer> callback) {
        player.sendMessage(ChatColor.YELLOW + promptMsg);

        ChatInputListener.inputMap.put(player.getUniqueId(), input -> {
            try {
                int value = Integer.parseInt(input);
                callback.accept(value);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid number. Please enter a valid integer.");
                intPrompt(player, promptMsg, callback);
            }
        });
    }

    public static void optionPrompt(Player player, String promptMsg, Option... options) {
        player.sendMessage(ChatColor.YELLOW + promptMsg);

        for (Option option : options) {
            TextComponent clickable = new TextComponent("> " + capitalize(option.getLabel()));
            clickable.setColor(net.md_5.bungee.api.ChatColor.AQUA);
            clickable.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, option.getLabel()));
            player.spigot().sendMessage(clickable);
        }

        ChatInputListener.inputMap.put(player.getUniqueId(), input -> {
            String normalized = input.trim().toLowerCase();

            for (Option option : options) {
                if (option.getLabel().equalsIgnoreCase(normalized)) {
                    option.getAction().run();
                    return;
                }
            }

            player.sendMessage(ChatColor.RED + "Invalid option. Please choose from the list.");
            optionPrompt(player, promptMsg, options);
        });
    }

    private static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
