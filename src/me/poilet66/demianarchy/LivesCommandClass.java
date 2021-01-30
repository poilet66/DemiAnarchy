package me.poilet66.demianarchy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesCommandClass implements CommandExecutor {

    private DemiAnarchy main;

    public LivesCommandClass(DemiAnarchy main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(!main.getPLM().getLivesMap().containsKey(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "Error retrieving lives.");
                    return true;
                }
                if(main.getPLM().getLivesMap().get(player.getUniqueId()) <= 0) {
                    player.sendMessage(String.format(ChatColor.GREEN + "You have " + ChatColor.RED + "%d" + ChatColor.GREEN + " lives.", main.getPLM().getLivesMap().get(player.getUniqueId())));
                    return true;
                }
                player.sendMessage(String.format(ChatColor.GREEN + "You have %d lives.", main.getPLM().getLivesMap().get(player.getUniqueId())));
                return true;
            }
        }
        if(args[0].equalsIgnoreCase("set")) {
            if(!sender.hasPermission("demianarchy.lives.set")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                return true;
            }
            if(args.length < 3) {
                sender.sendMessage(ChatColor.RED + "You have not provided enough arguments.");
                return true;
            }
            if(Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(ChatColor.RED + "That player is not online.");
                return true;
            }
            try {
                main.getPLM().setPlayerLife(Bukkit.getPlayer(args[1]).getUniqueId(), Integer.parseInt(args[2]));
                sender.sendMessage(String.format(ChatColor.GREEN + "Set %s to %s lives.", args[1], args[2]));
            } catch (NumberFormatException NFE) {
                sender.sendMessage(ChatColor.RED + "The amount you have entered is not a number.");
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Something went wrong! Contact and admin.");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
