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
import org.bukkit.inventory.ItemStack;

public class KickGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "Kick - Choose a reason - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        ItemStack spam = new ItemStack(Material.OAK_SIGN);
        var spamMeta = spam.getItemMeta();
        if (spamMeta != null) {
            spamMeta.setDisplayName("Spamming");
            spam.setItemMeta(spamMeta);
        }
        gui.setItem(10, spam);

        ItemStack insult = new ItemStack(Material.PLAYER_HEAD);
        var insultMeta = insult.getItemMeta();
        if (insultMeta != null) {
            insultMeta.setDisplayName(ChatColor.WHITE + "Rude / Toxic / Insulting");
            insult.setItemMeta(insultMeta);
        }
        gui.setItem(11, insult);

        ItemStack language = new ItemStack(Material.OAK_SIGN);
        var languageMeta = language.getItemMeta();
        if (languageMeta != null) {
            languageMeta.setDisplayName("Inappropriate Language");
            language.setItemMeta(languageMeta);
        }
        gui.setItem(12, language);

        ItemStack blank = new ItemStack(Material.PAPER);
        var blankMeta = blank.getItemMeta();
        if (blankMeta != null) {
            blankMeta.setDisplayName("None");
            blank.setItemMeta(blankMeta);
        }
        gui.setItem(15, blank);

        ItemStack custom = new ItemStack(Material.BOOK);
        var customMeta = custom.getItemMeta();
        if (customMeta != null) {
            customMeta.setDisplayName("Enter your own");
            custom.setItemMeta(customMeta);
        }
        gui.setItem(16, custom);

        ItemStack ads = new ItemStack(Material.BELL);
        var adsMeta = ads.getItemMeta();
        if (adsMeta != null) {
            adsMeta.setDisplayName("Advertising");
            ads.setItemMeta(adsMeta);
        }
        gui.setItem(19, ads);

        ItemStack staff = new ItemStack(Material.COMMAND_BLOCK);
        var staffMeta = staff.getItemMeta();
        if (staffMeta != null) {
            staffMeta.setDisplayName(ChatColor.WHITE + "Staff Disrespect");
            staff.setItemMeta(staffMeta);
        }
        gui.setItem(20, staff);

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
                ModerationGUI.open(clicker, target);
                clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
            } else {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
            }
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
            target.kick(Component.text("Kicked: Spamming"));
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for Spamming.");
        }

        if (slot == 11) {
            target.kick(Component.text("Kicked: Rude / Toxic / Insulting"));
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for being Rude / Toxic / Insulting.");
        }

        if (slot == 12) {
            target.kick(Component.text("Kicked: Inappropriate Language"));
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for Inappropriate Language.");
        }

        if (slot == 15) {
            target.kick();
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName());
        }

        if (slot == 16) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the custom kick reason in chat.");

            ChatInputListener.inputMap.put(clicker.getUniqueId(), reason -> {
                if (target.isOnline()) {
                    target.kick(Component.text("Kicked: " + reason));
                    clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                    clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for: " + reason);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player is no longer online.");
                }
            });
        }

        if (slot == 19) {
            target.kick(Component.text("Kicked: Advertising"));
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for Advertising.");
        }

        if (slot == 20) {
            target.kick(Component.text("Kicked: Staff Disrespect"));
            clicker.playSound(clicker.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            clicker.sendMessage(ChatColor.GREEN + "Kicked " + target.getName() + " for Staff Disrespect.");
        }
    }

}
