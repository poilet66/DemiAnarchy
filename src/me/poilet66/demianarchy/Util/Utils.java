package me.poilet66.demianarchy.Util;

import me.poilet66.demianarchy.DemiAnarchy;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {

    public static double getPointDistanceFromPlayer(Player player, Location location) {
        return player.getLocation().distance(location);
    }

    public static void messageAll(String message, DemiAnarchy main) {
        for(Player player : main.getServer().getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

}
