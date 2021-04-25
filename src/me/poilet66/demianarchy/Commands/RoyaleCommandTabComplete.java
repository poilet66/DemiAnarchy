package me.poilet66.demianarchy.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoyaleCommandTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String lbl, String[] args) {
        if(!sender.hasPermission("demianarchy.royale.start")) {
            return null;
        }
        if(args.length == 1) {
            return Arrays.asList("start", "forcestop").stream()
                    .filter(s -> s.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
