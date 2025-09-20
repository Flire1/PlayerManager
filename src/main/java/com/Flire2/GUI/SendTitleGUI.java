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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SendTitleGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Config Title - ";

    private static final Map<UUID, String> pTitle = new HashMap<>();
    private static final Map<UUID, String> sTitle = new HashMap<>();

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        String currentTitle = pTitle.getOrDefault(target.getUniqueId(), "None");
        String currentSubtitle = sTitle.getOrDefault(target.getUniqueId(), "None");

        gui.setItem(10, GUICommon.createItem(Material.OAK_SIGN, "Text", ChatColor.GRAY + "Current Text: " + currentTitle));
        gui.setItem(11, GUICommon.createItem(Material.OAK_HANGING_SIGN, "Subtitle", ChatColor.GRAY + "Current Text: " + currentSubtitle));

        gui.setItem(16, GUICommon.createItem(Material.GREEN_CONCRETE, "Send Title"));

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
            clicker.sendMessage(ChatColor.YELLOW + "Please type the title text in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), input -> {
                if (target != null) {
                    pTitle.put(target.getUniqueId(), input);
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    clicker.sendMessage(ChatColor.GREEN + "Title set for " + target.getName() + ": " + input);
                    open(clicker, target);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player not found.");
                }
            });
        }

        if (slot == 11) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the subtitle text in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), input -> {
                if (target != null) {
                    sTitle.put(target.getUniqueId(), input);
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    clicker.sendMessage(ChatColor.GREEN + "Subtitle set for " + target.getName() + ": " + input);
                    open(clicker, target);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player not found.");
                }
            });
        }

        if (slot == 16) {
            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
                return;
            }

            String titleText = pTitle.get(target.getUniqueId());
            String subtitleText = sTitle.get(target.getUniqueId());

            target.sendTitle(titleText, subtitleText, 20, 70, 20);
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }
}
