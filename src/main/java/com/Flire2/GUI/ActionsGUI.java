package com.Flire2.GUI;

import com.Flire2.ActionsManager.Flight;
import com.Flire2.ActionsManager.Freeze;
import com.Flire2.ActionsManager.Godmode;
import com.Flire2.GUICommon;
import com.Flire2.PlayerDataManagerTemp;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ActionsGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Player Actions - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.ENDER_PEARL, "Teleport", ChatColor.GRAY + "> Click to teleport to player!", ChatColor.GRAY + "> Right click to teleport player to you!"));
        gui.setItem(11, GUICommon.createItem(Material.ICE, "Freeze", ChatColor.GRAY + "> Click to freeze player!", ChatColor.GRAY + "> Right click unfreeze!"));
        gui.setItem(12, GUICommon.createItem(Material.ENCHANTED_GOLDEN_APPLE, ChatColor.WHITE + "Godmode", ChatColor.GRAY + "> Click to enable godmode!", ChatColor.GRAY + "> Right click to disable godmode!"));
        gui.setItem(13, GUICommon.createItem(Material.FEATHER, "Flight", ChatColor.GRAY + "> Click to enable flight!", ChatColor.GRAY + "> Right click to disable flight!"));

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

        if (slot == 4) {
            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
                return;
            }

            if (click == ClickType.RIGHT) {
                EditStatsGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        if (slot == 7) {
            if (target != null) {
                PlayerManagerGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }

        if (slot == 8) {
            clicker.closeInventory();
            clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
        }

        if (slot == 10) {
            if (click == ClickType.RIGHT) {
                clicker.teleport(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                target.teleport(clicker);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        if (slot == 11) {
            if (click == ClickType.RIGHT) {
                PlayerDataManagerTemp.set(target.getUniqueId(), "isFrozen", false);
                Freeze.updateFreezeStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                PlayerDataManagerTemp.set(target.getUniqueId(), "isFrozen", true);
                Freeze.updateFreezeStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        if (slot == 12) {
            if (click == ClickType.RIGHT) {
                PlayerDataManagerTemp.set(target.getUniqueId(), "hasGodmode", false);
                Godmode.updateGodmodeStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                PlayerDataManagerTemp.set(target.getUniqueId(), "hasGodmode", true);
                Godmode.updateGodmodeStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }

        if (slot == 13) {
            if (click == ClickType.RIGHT) {
                PlayerDataManagerTemp.set(target.getUniqueId(), "allowFlight", false);
                Flight.updateFlightStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                PlayerDataManagerTemp.set(target.getUniqueId(), "allowFlight", true);
                Flight.updateFlightStatus(target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            }
        }
    }
}
