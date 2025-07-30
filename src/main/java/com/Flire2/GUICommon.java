package com.Flire2;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICommon {

    public static ItemStack createPlayerInfoHead(Player target) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if (meta == null) return head;

        meta.setOwningPlayer(target);
        meta.setDisplayName(ChatColor.GREEN + target.getName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Health: " + target.getHealth() + "/" + target.getMaxHealth());
        lore.add(ChatColor.GOLD + "Hunger: " + target.getFoodLevel());
        lore.add(ChatColor.GOLD + "Saturation: " + target.getSaturation());
        lore.add(ChatColor.AQUA + "Gamemode: " + target.getGameMode());
        lore.add(ChatColor.GREEN + "World: " + target.getWorld().getName());

        int x = target.getLocation().getBlockX();
        int y = target.getLocation().getBlockY();
        int z = target.getLocation().getBlockZ();
        lore.add(ChatColor.GRAY + "Coordinates: " + x + ", " + y + ", " + z);

        Biome biome = target.getLocation().getBlock().getBiome();
        lore.add(ChatColor.GREEN + "Biome: " + biome.getKey().getKey());

        World.Environment env = target.getWorld().getEnvironment();
        String dimension = switch (env) {
            case NORMAL -> "Overworld";
            case NETHER -> "Nether";
            case THE_END -> "The End";
            default -> "Unknown";
        };
        lore.add(ChatColor.RED + "Dimension: " + dimension);

        lore.add(ChatColor.GREEN + "Ping: " + target.getPing() + "ms");
        lore.add(ChatColor.GRAY + "UUID: " + target.getUniqueId());
        lore.add("");
        lore.add(ChatColor.GRAY + "> Click to refresh! \uD83D\uDD04");
        lore.add(ChatColor.GRAY + "> Right Click to edit!");

        meta.setLore(lore);
        head.setItemMeta(meta);
        return head;
    }

    public static ItemStack createItem(Material material, String name, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            if (loreLines != null && loreLines.length > 0) {
                meta.setLore(Arrays.asList(loreLines));
            }
            item.setItemMeta(meta);
        }

        return item;
    }
}
