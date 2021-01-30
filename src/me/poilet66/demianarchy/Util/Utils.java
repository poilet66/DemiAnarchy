package me.poilet66.demianarchy.Util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {

    public static double getPointDistanceFromPlayer(Player player, Location location) {
        return player.getLocation().distance(location);
    }

}
