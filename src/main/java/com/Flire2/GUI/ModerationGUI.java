package com.Flire2.GUI;

import com.Flire2.GUICommon;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ModerationGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Player Moderation - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        ItemStack back = new ItemStack(Material.ARROW);
        var backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.WHITE + "Back");
            back.setItemMeta(backMeta);
        }
        gui.setItem(7, back);

        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.ORANGE_CONCRETE, "Kick"));
        gui.setItem(11, GUICommon.createItem(Material.RED_CONCRETE, "Ban"));

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
            if (target != null) {
                KickGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }

        if (slot == 11) {
            if (target != null) {
                BanGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }
    }
}
