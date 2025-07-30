package com.Flire2.GUI;

import com.Flire2.GUICommon;
import com.Flire2.Option;
import com.Flire2.PromptUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class EditStatsGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Edit Stats - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        gui.setItem(4, GUICommon.createPlayerInfoHead(target));
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.RED_DYE, ChatColor.WHITE + "Edit Health", ChatColor.GRAY + "> Click to edit!", ChatColor.GRAY + "> Right Click to heal!", ChatColor.GRAY + "> Shift Click to kill!"));


        gui.setItem(13, GUICommon.createItem(Material.BEACON, ChatColor.WHITE + "Edit Gamemode", ChatColor.GRAY + "> Click to edit!"));

        viewer.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player clicker = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();
        if (!title.startsWith(GUI_TITLE_PREFIX)) return;

        e.setCancelled(true);

        int slot = e.getRawSlot();
        ClickType click = e.getClick();
        String targetName = title.replace(GUI_TITLE_PREFIX, "");
        Player target = Bukkit.getPlayerExact(targetName);

        if (slot == 4 && target != null) {
            open(clicker, target);
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }

        if (slot == 7 && target != null) {
            PlayerManagerGUI.open(clicker, target);
            clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
        }

        if (slot == 8) {
            clicker.closeInventory();
            clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
        }

        if (target == null) {
            clicker.sendMessage(ChatColor.RED + "Target player not found.");
            return;
        }

        if (slot == 10) {
            if (click == ClickType.RIGHT) {
                target.setHealth(target.getMaxHealth());
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else if (click == ClickType.SHIFT_LEFT) {
                target.setHealth(0);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                clicker.closeInventory();

                PromptUtils.doublePrompt(clicker, "Enter the new health value (must be positive):", value -> {
                    if (!target.isOnline()) {
                        clicker.sendMessage(ChatColor.RED + "Target player is no longer online.");
                        return;
                    }

                    double maxHealth = target.getMaxHealth();
                    if (value > maxHealth) {
                        clicker.sendMessage(ChatColor.RED + "Health cannot exceed target's max health (" + maxHealth + ").");
                        PromptUtils.doublePrompt(clicker, "Enter a value ≤ " + maxHealth + ":", val -> {
                            target.setHealth(val);
                            clicker.sendMessage(ChatColor.GREEN + "Set health to " + val);
                            open(clicker, target);
                        });
                        return;
                    }

                    target.setHealth(value);
                    clicker.sendMessage(ChatColor.GREEN + "Set health to " + value);
                    open(clicker, target);
                });
            }
        }

        if (slot == 13) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Select one of the following options:");
            if (!target.isOnline()) {
                clicker.sendMessage(ChatColor.RED + "Target player is no longer online.");
                return;
            }
            PromptUtils.optionPrompt(clicker, "Choose a gamemode:",
                    new Option("Creative", () -> target.setGameMode(GameMode.CREATIVE)),
                    new Option("Survival", () -> target.setGameMode(GameMode.SURVIVAL)),
                    new Option("Adventure", () -> target.setGameMode(GameMode.ADVENTURE)),
                    new Option("Spectator", () -> target.setGameMode(GameMode.SPECTATOR)),
                    new Option("Cancel", () -> clicker.sendMessage(ChatColor.GRAY + "Cancelled."))
            );
        }
    }
}
