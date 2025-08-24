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

import java.lang.annotation.Target;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanIPGUI implements Listener {

    private static final String GUI_TITLE_PREFIX = "IP Ban - Choose a reason - ";

    public static void open(Player viewer, Player target) {
        String title = GUI_TITLE_PREFIX + target.getName();
        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Player head
        gui.setItem(4, GUICommon.createPlayerInfoHead(target));

        // Back
        gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

        // Close
        gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

        gui.setItem(10, GUICommon.createItem(Material.COMMAND_BLOCK, ChatColor.WHITE + "Cheating / Hacking", "", ChatColor.GRAY + "> Click to choose duration!"));
        gui.setItem(11, GUICommon.createItem(Material.STRING, "Repeated Rule Breaking", "", ChatColor.GRAY + "> Click to choose duration!"));
        gui.setItem(12, GUICommon.createItem(Material.OAK_SIGN, "Exploiting Bugs", "", ChatColor.GRAY + "> Click to choose duration!"));
        gui.setItem(15, GUICommon.createItem(Material.PAPER, "None", "", ChatColor.GRAY + "> Click to choose duration!"));
        gui.setItem(16, GUICommon.createItem(Material.BOOK, "Enter your own", "", ChatColor.GRAY + "> Click to enter custom reason!"));
        gui.setItem(19, GUICommon.createItem(Material.BELL, "Ban Evasion", "", ChatColor.GRAY + "> Click to choose duration!"));
        gui.setItem(20, GUICommon.createItem(Material.COMMAND_BLOCK, ChatColor.WHITE + "Harassment / Threats", "", ChatColor.GRAY + "> Click to choose duration!"));

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
            BanContext.selectedReason.put(clicker.getUniqueId(), "Cheating / Hacking");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 11) {
            BanContext.selectedReason.put(clicker.getUniqueId(), "Repeated Rule Breaking");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 12) {
            BanContext.selectedReason.put(clicker.getUniqueId(), "Exploiting Bugs");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 19) {
            BanContext.selectedReason.put(clicker.getUniqueId(), "Ban Evasion");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 20) {
            BanContext.selectedReason.put(clicker.getUniqueId(), "Harassment / Threats");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 15) {
            BanContext.selectedReason.put(clicker.getUniqueId(), "None");
            BanIPGUIDuration.open(clicker, target);
        }
        if (slot == 16) {
            clicker.closeInventory();
            clicker.sendMessage(ChatColor.YELLOW + "Please type the custom reason in chat.");
            ChatInputListener.inputMap.put(clicker.getUniqueId(), reason -> {
                BanContext.selectedReason.put(clicker.getUniqueId(), reason);
                BanIPGUIDuration.open(clicker, target);
            });
        }
    }

    public static class BanIPGUIDuration implements Listener {

        private static final String GUI_TITLE_PREFIX = "IP Ban - Choose a duration - ";

        public static void open(Player viewer, Player target) {
            String title = GUI_TITLE_PREFIX + target.getName();
            Inventory gui = Bukkit.createInventory(null, 27, title);

            // Player head
            gui.setItem(4, GUICommon.createPlayerInfoHead(target));

            // Back
            gui.setItem(7, GUICommon.createItem(Material.ARROW, ChatColor.WHITE + "Back"));

            // Close
            gui.setItem(8, GUICommon.createItem(Material.BARRIER, ChatColor.RED + "Close"));

            gui.setItem(15, GUICommon.createItem(Material.PAPER, "Permanent"));
            gui.setItem(16, GUICommon.createItem(Material.BOOK, "Enter your own"));

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
                } else {
                    open(clicker, target);
                }
            }

            if (slot == 7) {
                if (target != null) {
                    ModerationGUI.open(clicker, target);
                } else {
                    clicker.sendMessage(ChatColor.RED + "Target player not found.");
                }
            }

            if (slot == 8) {
                clicker.closeInventory();
            }

            if (target == null) {
                clicker.sendMessage(ChatColor.RED + "Target player not found.");
                return;
            }

            if (slot == 15) {
                String reason = BanContext.selectedReason.getOrDefault(clicker.getUniqueId(), "Banned");
                String ip = target.getAddress().getAddress().getHostAddress();

                Bukkit.getBanList(BanList.Type.IP).addBan(ip, reason, null, clicker.getName());
                target.kick(Component.text("IP Banned permanently: " + reason));
                clicker.sendMessage(ChatColor.GREEN + "Permanently IP banned " + target.getName() + " (" + ip + ") for: " + reason);

                BanContext.selectedReason.remove(clicker.getUniqueId());
            }

            if (slot == 16) {
                clicker.closeInventory();
                clicker.sendMessage(ChatColor.YELLOW + "Please type the custom duration (e.g., '3d' or '1h') in chat.");

                ChatInputListener.inputMap.put(clicker.getUniqueId(), duration -> {
                    String reason = BanContext.selectedReason.getOrDefault(clicker.getUniqueId(), "Banned");
                    String ip = target.getAddress().getAddress().getHostAddress();

                    Date expiry = parseDuration(duration);
                    if (expiry == null) {
                        clicker.sendMessage(ChatColor.RED + "Invalid duration format.");
                        return;
                    }

                    Bukkit.getBanList(BanList.Type.IP).addBan(ip, reason, expiry, clicker.getName());
                    target.kick(Component.text("IP Banned: " + reason + " (Until: " + expiry + ")"));
                    clicker.sendMessage(ChatColor.GREEN + "IP banned " + target.getName() + " (" + ip + ") for: " + reason + " (Until: " + expiry + ")");

                    BanContext.selectedReason.remove(clicker.getUniqueId());
                });
            }
        }

        private Date parseDuration(String input) {
            try {
                long now = System.currentTimeMillis();
                long millis = 0;

                if (input.endsWith("d")) millis = Long.parseLong(input.replace("d", "")) * 86400000L;
                else if (input.endsWith("h")) millis = Long.parseLong(input.replace("h", "")) * 3600000L;
                else if (input.endsWith("m")) millis = Long.parseLong(input.replace("m", "")) * 60000L;
                else return null;

                return new Date(now + millis);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class BanContext implements Listener {
        public static final Map<UUID, String> selectedReason = new HashMap<>();
    }
}
