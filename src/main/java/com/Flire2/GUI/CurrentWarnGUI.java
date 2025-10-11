package com.Flire2.GUI;

import com.Flire2.GUICommon;
import com.Flire2.PlayerDataManagerPerm;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class CurrentWarnGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Current Warnings - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();

        List<Map<String, Object>> warnings = (List<Map<String, Object>>) PlayerDataManagerPerm.get(target.getUniqueId(), "warnings");
        int warningCount = warnings != null ? warnings.size() : 0;

        int rows = Math.min(6, Math.max(3, ((warningCount - 1) / 7) + 3));

        Inventory gui = Bukkit.createInventory(null, rows * 9, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back button
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close button
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        // Display warnings
        if (warnings == null || warnings.isEmpty()) {
            gui.setItem(13, GUICommon.createItem(Material.PAPER, ChatColor.GRAY + "No warnings"));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int slot = 9;
            int index = 0;

            for (Map<String, Object> warning : warnings) {
                String reason = (String) warning.getOrDefault("reason", "Unknown");
                String issuer = (String) warning.getOrDefault("issuer", "Unknown");
                long time = (long) warning.getOrDefault("time", System.currentTimeMillis());
                String dateStr = sdf.format(new Date(time));

                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.RED + reason);
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Issuer: " + issuer,
                        ChatColor.GRAY + "Time: " + dateStr,
                        ChatColor.DARK_GRAY + "Index: " + index,
                        ChatColor.YELLOW + "Shift + Right-Click to delete"
                ));
                item.setItemMeta(meta);

                gui.setItem(slot, item);
                slot++;
                index++;

                if (slot % 9 == 8) slot += 3;
                if (slot >= rows * 9 - 9) break;
            }
        }

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

        Inventory inv = e.getInventory();
        ItemStack clicked = inv.getItem(slot);

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
            return;
        }

        if (clicked != null && clicked.getType() == Material.ARROW) {
            if (target != null) {
                PlayerManagerGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
            return;
        }

        if (clicked != null && clicked.getType() == Material.BARRIER) {
            clicker.closeInventory();
            clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            return;
        }

        if (clicked != null && clicked.getType() == Material.PAPER && click.isShiftClick() && click.isRightClick()) {
            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
                return;
            }

            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || meta.getLore() == null) return;

            int index = -1;
            for (String line : meta.getLore()) {
                if (line.contains("Index:")) {
                    try {
                        index = Integer.parseInt(ChatColor.stripColor(line).split(": ")[1]);
                    } catch (Exception ignored) {}
                }
            }

            if (index >= 0) {
                List<Map<String, Object>> warnings = (List<Map<String, Object>>) PlayerDataManagerPerm.get(target.getUniqueId(), "warnings");
                if (warnings != null && index < warnings.size()) {
                    warnings.remove(index);
                    PlayerDataManagerPerm.set(target.getUniqueId(), "warnings", warnings);
                    clicker.sendMessage(ChatColor.GREEN + "Deleted warning #" + (index + 1) + " for " + target.getName());
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                    open(clicker, target);
                }
            }
        }
    }
}
