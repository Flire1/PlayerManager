package com.Flire2.GUI;

import com.Flire2.GUICommon;
import com.Flire2.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Inventory Menu - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.CHEST, "View Inventory", ChatColor.GOLD + "> This is an experimental feature. Expect some bugs!"));
        gui.setItem(11, GUICommon.createItem(Material.ENDER_CHEST, "View Ender Chest"));

        gui.setItem(16, GUICommon.createItem(Material.BARRIER, ChatColor.WHITE + "Clear Inventory"));


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
                PlayerInventory.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }

        if (slot == 11) {
            if (target != null) {
                clicker.openInventory(target.getEnderChest());
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }

        if (slot == 16) {
            if (target != null) {
                target.getInventory().clear();
                target.getInventory().setArmorContents(null);
                target.getInventory().setItemInOffHand(null);
                target.updateInventory();
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }
    }

    public static class PlayerInventory implements Listener {
        private static final String GUI_TITLE_PREFIX = "Target Inventory - ";
        private static final Map<UUID, UUID> viewingMap = new HashMap<>();

        public static void open(Player viewer, Player target) {
            String title = GUI_TITLE_PREFIX + target.getName();
            Inventory gui = Bukkit.createInventory(null, 54, title);

            gui.setItem(0, target.getInventory().getHelmet());
            gui.setItem(1, target.getInventory().getChestplate());
            gui.setItem(2, target.getInventory().getLeggings());
            gui.setItem(3, target.getInventory().getBoots());

            gui.setItem(4, GUICommon.createItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.GRAY + "← Armor | Offhand →"));
            gui.setItem(5, target.getInventory().getItemInOffHand());
            gui.setItem(6, GUICommon.createItem(Material.WHITE_STAINED_GLASS_PANE, " "));
            gui.setItem(7, GUICommon.createItem(Material.WHITE_STAINED_GLASS_PANE, " "));
            gui.setItem(8, GUICommon.createItem(Material.WHITE_STAINED_GLASS_PANE, " "));

            for (int i = 0; i < 9; i++) {
                gui.setItem(9 + i, GUICommon.createItem(Material.WHITE_STAINED_GLASS_PANE, "Layout:", ChatColor.GRAY + "↑ Armor & Offhand", ChatColor.GRAY + "↓ Next 3 rows are the target's inventory", ChatColor.GRAY + "↓ Last row is the target's hotbar"));
            }

            for (int i = 0; i < 27; i++) {
                gui.setItem(18 + i, target.getInventory().getItem(i + 9));
            }

            for (int i = 0; i < 9; i++) {
                gui.setItem(45 + i, target.getInventory().getItem(i));
            }

            viewer.openInventory(gui);
            viewingMap.put(viewer.getUniqueId(), target.getUniqueId());
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player viewer)) return;

            String title = event.getView().getTitle();
            if (!title.startsWith(GUI_TITLE_PREFIX)) return;

            Inventory clickedInv = event.getClickedInventory();
            Inventory topInv = event.getView().getTopInventory();

            if (clickedInv == null || clickedInv != topInv) return;

            int slot = event.getRawSlot();
            if (slot >= 4 && slot <= 17) {
                ItemStack item = event.getClickedInventory().getItem(slot);
                if (item != null && item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                    event.setCancelled(true);
                    return;
                }
            }

            UUID targetUUID = viewingMap.get(viewer.getUniqueId());
            if (targetUUID == null) {
                viewer.sendMessage(ChatColor.RED + "Target player not found.");
                event.setCancelled(true);
                return;
            }

            Player target = Bukkit.getPlayer(targetUUID);
            if (target == null || !target.isOnline()) {
                viewer.sendMessage(ChatColor.RED + "Target is offline.");
                event.setCancelled(true);
                return;
            }

            event.setCancelled(false);

            Bukkit.getScheduler().runTaskLater(PlayerManager.getInstance(), () -> {
                ItemStack updated = event.getView().getTopInventory().getItem(slot);
                switch (slot) {
                    case 0 -> target.getInventory().setHelmet(updated);
                    case 1 -> target.getInventory().setChestplate(updated);
                    case 2 -> target.getInventory().setLeggings(updated);
                    case 3 -> target.getInventory().setBoots(updated);
                    case 5 -> target.getInventory().setItemInOffHand(updated);
                    case 18,19,20,21,22,23,24,25,26,
                         27,28,29,30,31,32,33,34,35,
                         36,37,38,39,40,41,42,43,44 -> {
                        int index = slot - 18 + 9;
                        target.getInventory().setItem(index, updated);
                    }
                    case 45,46,47,48,49,50,51,52,53 -> {
                        int index = slot - 45;
                        target.getInventory().setItem(index, updated);
                    }
                }
            }, 1L);
        }
    }
}
