package com.Flire2.GUI;

import com.Flire2.ChatInputListener;
import com.Flire2.GUICommon;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SendActionBarGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Config Action Bar - ";

    private static final Map<UUID, String> actionBarText = new HashMap<>();

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        String currentText = actionBarText.getOrDefault(target.getUniqueId(), ChatColor.GRAY + "None");
        gui.setItem(10, GUICommon.createItem(Material.OAK_SIGN, ChatColor.YELLOW + "Set Action Bar Text", ChatColor.GRAY + "Current: " + currentText));

        gui.setItem(16, GUICommon.createItem(Material.GREEN_CONCRETE, "Send Action Bar"));

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
            clicker.sendMessage(ChatColor.YELLOW + "Please type the new action bar text in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), input -> {
                actionBarText.put(target.getUniqueId(), input);
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                clicker.sendMessage(ChatColor.GREEN + "Action bar text set to " + input);
                open(clicker, target);
            });
        }

        if (slot == 16) {
            String message = actionBarText.getOrDefault(target.getUniqueId(), "");
            if (!message.isEmpty()) {
                target.sendActionBar(Component.text(message));
                clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            } else {
                clicker.sendMessage(ChatColor.RED + "No action bar text set for this player.");
            }
        }
    }
}
