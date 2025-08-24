package com.Flire2.GUI;

import com.Flire2.ChatInputListener;
import com.Flire2.GUICommon;
import com.Flire2.PlayerDataManagerPerm;
import com.Flire2.PlayerDataManagerTemp;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class IdentityGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Player Identity - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.OAK_SIGN, "Edit Display Name / Nickname", "", ChatColor.WHITE + "Current Display Name: " + ChatColor.GRAY + target.getDisplayName(), "", ChatColor.GRAY + "Does NOT appear in tab list!"));
        gui.setItem(11, GUICommon.createItem(Material.OAK_HANGING_SIGN, "Edit Tab List Name", "", ChatColor.WHITE + "Current Tab List Name: " + ChatColor.GRAY + target.getPlayerListName()));

        gui.setItem(16, GUICommon.createItem(Material.BARRIER, ChatColor.WHITE + "Reset Identity", ChatColor.GRAY + "Requires player to rejoin!"));

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
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the new display name in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), name -> {
                if (target.isOnline()) {
                    target.setDisplayName(name);
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    clicker.sendMessage(ChatColor.GREEN + "" + target + "'s display name changed to" + name);
                    PlayerDataManagerPerm.set(target.getUniqueId(), "displayName", name);
                    open(clicker, target);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player is no longer online.");
                }
            });
        }

        if (slot == 11) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the new tab list name in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), tabname -> {
                if (target.isOnline()) {
                    target.setPlayerListName(tabname);
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    clicker.sendMessage(ChatColor.GREEN + "" + target + "'s tab list name changed to" + tabname);
                    PlayerDataManagerPerm.set(target.getUniqueId(), "tabListName", tabname);
                    open(clicker, target);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player is no longer online.");
                }
            });
        }

        if (slot == 16) {
            PlayerDataManagerPerm.set(target.getUniqueId(), "displayName", null);
            PlayerDataManagerPerm.set(target.getUniqueId(), "tabListName", null);
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            open(clicker, target);
        }
    }
}
