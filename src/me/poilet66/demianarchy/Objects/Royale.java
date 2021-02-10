package me.poilet66.demianarchy.Objects;

import me.poilet66.demianarchy.DemiAnarchy;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Random;
import java.util.Set;

public class Royale {

    private final DemiAnarchy main;
    private Set<Player> players;
    Random rnd = new Random();

    public Royale(DemiAnarchy main) {
        this.main = main;
        main.getLogger().info("Generating new Royale word... prepare for some lag.");
        start();
    }

    //starts the royale
    private void start() {
        //create new world, tp all players (if not god), shrink border
        if(!Bukkit.getServer().getWorld("royale").equals(null)) { //if old world exists, delete it
            main.getServer().unloadWorld(main.getServer().getWorld("royale"), true);
            World delete = main.getServer().getWorld("royale");
            File deleteFolder = delete.getWorldFolder();
            deleteWorld(deleteFolder);
        }
        World royale = new WorldCreator("royale").environment(World.Environment.NORMAL).createWorld(); //create new world

        for(Player player : main.getServer().getOnlinePlayers()) {
            /*if(player.hasPermission("demianarchy.royale.avoid")) {
                player.sendMessage(main.prefix + ChatColor.GOLD + "You avoided the royale.");
                continue;
            }*/
            //teleport player to random location in world
            player.teleport(randomLocInRadius(royale, main.getConfig().getInt("royaleRadius")));
        }
    }

    //ends the battle royale
    public void finish() {

    }

    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    private Location randomLocInRadius(World world, int radius) {
        Location ret = new Location(world, 0, 0, 0);
        ret.setX(rnd.nextInt(radius*2)-radius);
        ret.setZ(rnd.nextInt(radius*2)-radius);
        ret.setY(ret.getWorld().getHighestBlockYAt(ret.getBlockX(), ret.getBlockZ()));
        return ret;
    }
}
