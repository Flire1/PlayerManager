package com.Flire2.GUI;

import com.Flire2.ChatInputListener;
import com.Flire2.GUICommon;
import com.Flire2.PlayerDataManagerPerm;
import net.kyori.adventure.text.Component;
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

import java.util.*;

public class WarnGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Warn - Choose a reason - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.COMMAND_BLOCK, ChatColor.WHITE + "Spamming", "", ChatColor.GRAY + "> Click to warn!"));
        gui.setItem(11, GUICommon.createItem(Material.PAPER, "Disrespect", "", ChatColor.GRAY + "> Click to warn!"));
        gui.setItem(12, GUICommon.createItem(Material.BOOK, "Harassment", "", ChatColor.GRAY + "> Click to warn!"));

        gui.setItem(15, GUICommon.createItem(Material.OAK_SIGN, "Minor Offense", "", ChatColor.GRAY + "> Click to warn!"));
        gui.setItem(16, GUICommon.createItem(Material.WRITABLE_BOOK, "Custom Reason", "", ChatColor.GRAY + "> Enter in chat!"));

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

        // Player head
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

        // Back
        if (slot == 7) {
            if (target != null) {
                ModerationGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
        }

        // Close
        if (slot == 8) {
            clicker.closeInventory();
            clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
        }

        if (target == null) {
            clicker.sendMessage(ChatColor.RED + "Target player not found.");
            return;
        }

        if (slot == 10) {
            giveWarning(clicker, target, "Spamming");
        }

        if (slot == 11) {
            giveWarning(clicker, target, "Disrespect");
        }

        if (slot == 12) {
            giveWarning(clicker, target, "Harassment");
        }

        if (slot == 15) {
            giveWarning(clicker, target, "Minor Offense");
        }

        // Custom reason
        if (slot == 16) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the custom warning reason in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), reason -> {
                giveWarning(clicker, target, reason);
            });
        }
    }

    private void giveWarning(Player issuer, Player target, String reason) {
        UUID targetId = target.getUniqueId();
        UUID issuerId = issuer.getUniqueId();

        Map<String, Object> warning = new HashMap<>();
        warning.put("reason", reason);
        warning.put("issuer", issuer.getName());
        warning.put("time", System.currentTimeMillis());

        List<Map<String, Object>> warnings = (List<Map<String, Object>>) PlayerDataManagerPerm.get(targetId, "warnings");
        if (warnings == null) warnings = new ArrayList<>();

        warnings.add(warning);

        PlayerDataManagerPerm.set(targetId, "warnings", warnings);

        issuer.sendMessage(ChatColor.GREEN + "Warned " + target.getName() + " for: " + reason);
        target.sendMessage(ChatColor.RED + "You have been warned: " + ChatColor.YELLOW + reason);

        target.sendActionBar(Component.text(ChatColor.RED + "Warning: " + ChatColor.YELLOW + reason));

        issuer.playSound(issuer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }
}
