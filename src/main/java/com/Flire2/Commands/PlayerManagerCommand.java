package com.Flire2.Commands;

import com.Flire2.GUI.PlayerManagerGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerManagerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("PlayerManager.Commands.pm")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (!(sender instanceof Player)) return true;

        if (args.length < 1) {
            sender.sendMessage("Usage: /playermanager <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found");
            return true;
        }

        PlayerManagerGUI.open((Player) sender, target);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
