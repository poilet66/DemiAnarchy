package me.poilet66.demianarchy.Commands;

import me.poilet66.demianarchy.DemiAnarchy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCommand implements CommandExecutor {

    private final DemiAnarchy main;

    public LivesCommand(DemiAnarchy main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) { //checking own lives
            if(sender instanceof Player) { //need to be a player to check own lives
                Player player = (Player) sender;
                if(!main.getPLM().getLivesMap().containsKey(player.getUniqueId())) {
                    player.sendMessage(main.prefix + ChatColor.RED + "Error retrieving lives.");
                    return true;
                }
                if(main.getPLM().getLivesMap().get(player.getUniqueId()) <= 0) {
                    player.sendMessage(String.format(main.prefix + ChatColor.GREEN + "You have " + ChatColor.RED + "%d" + ChatColor.GREEN + " lives.", main.getPLM().getLivesMap().get(player.getUniqueId())));
                    return true;
                }
                player.sendMessage(String.format(main.prefix + ChatColor.GREEN + "You have %d lives.", main.getPLM().getLivesMap().get(player.getUniqueId())));
                return true;
            }
        }
        if(args[0].equalsIgnoreCase("set")) { //setting lives
            if(!sender.hasPermission("demianarchy.lives.set")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                return true;
            }
            if(args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You have not provided enough arguments.");
                return true;
            }
            if(!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "That player has not played before.");
                return true;
            }
            try {
                main.getPLM().setPlayerLife(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Integer.parseInt(args[2]));
                sender.sendMessage(String.format(main.prefix + ChatColor.GREEN + "Set %s to %s lives.", args[1], args[2]));
            } catch (NumberFormatException NFE) {
                sender.sendMessage(ChatColor.RED + "The amount you have entered is not a number.");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Something went wrong! Contact an admin.");
                e.printStackTrace();
            }
            return true;
        }
        if(Bukkit.getOfflinePlayer(args[0]) != null) { //checking someone else's lives
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if(!player.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + "That player has not played before.");
                return true;
            }
            if(!main.getPLM().getLivesMap().containsKey(player.getUniqueId())) {
                sender.sendMessage(main.prefix + ChatColor.RED + "Error retrieving lives.");
                return true;
            }
            if(main.getPLM().getLivesMap().get(player.getUniqueId()) <= 0) {
                sender.sendMessage(String.format(main.prefix + ChatColor.GREEN + "%s has " + ChatColor.RED + "%d" + ChatColor.GREEN + " lives.",player.getName(), main.getPLM().getLivesMap().get(player.getUniqueId())));
                return true;
            }
            sender.sendMessage(String.format(main.prefix + ChatColor.GREEN + "%s has %d lives.",player.getName(), main.getPLM().getLivesMap().get(player.getUniqueId())));
            return true;
        }
        return false;
    }
}
