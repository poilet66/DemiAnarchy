package me.poilet66.demianarchy.Commands;

import me.poilet66.demianarchy.DemiAnarchy;
import me.poilet66.demianarchy.Objects.Royale;
import me.poilet66.demianarchy.Util.Countdown;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RoyaleCommand implements CommandExecutor {

    private final DemiAnarchy main;

    public RoyaleCommand(DemiAnarchy main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args[0].equalsIgnoreCase("start")) {
            if(!sender.hasPermission("demianarhcy.royale.start")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                return true;
            }
            if(args.length < 2) {
                sender.sendMessage(ChatColor.RED + "You have not provided enough arguments.");
                return true;
            }
            try {
                int minutesToStart = Integer.parseInt(args[1]);
                sender.sendMessage(String.format(ChatColor.DARK_AQUA + "Royale scheduled for %s minutes time.", args[1]));

                Countdown countdown = new Countdown(minutesToStart, 60, main) {

                    @Override
                    public void count(int current) {
                        for(Player player : main.getServer().getOnlinePlayers()) {
                            player.sendMessage(String.format(main.prefix + ChatColor.BLUE + "A royale will begin in %s minutes", current+=1));
                        }
                    }

                    @Override
                    public void finish() {
                        for(Player player : main.getServer().getOnlinePlayers()) {
                            player.sendMessage(main.prefix + ChatColor.BLUE + "Royale beginning!");
                        }
                        Royale royale = new Royale(main);
                    }

                };
                countdown.start();
                return true;
            } catch(NumberFormatException NFE) {
                sender.sendMessage(ChatColor.RED + "The amount you entered is not a number.");
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "Please enter a valid subcommand.");
        return true;
    }
}
