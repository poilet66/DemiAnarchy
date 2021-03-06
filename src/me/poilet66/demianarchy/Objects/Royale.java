package me.poilet66.demianarchy.Objects;

import me.poilet66.demianarchy.DemiAnarchy;
import me.poilet66.demianarchy.Util.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Royale {

    private final DemiAnarchy main;
    private final static double PI = 3.1415;
    private Set<Player> players;
    Random rnd = new Random();

    public Royale(DemiAnarchy main) {
        this.main = main;
        this.players = new HashSet<Player>();
        main.getLogger().info("Generating new Royale word... prepare for some lag.");
        start();
    }

    //starts the royale
    private void start() {
        //create new world, tp all players (if not god), shrink border
        if(!Objects.equals(Bukkit.getServer().getWorld("royale"), null)) { //if old world exists, delete it
            main.getLogger().info("Pre-existing royale world locating, deleting before generating a new one");
            main.getServer().unloadWorld(main.getServer().getWorld("royale"), true);
            World delete = main.getServer().getWorld("royale");
            File deleteFolder = delete.getWorldFolder();
            deleteWorld(deleteFolder);
        }
        World royale = new WorldCreator("royale").environment(World.Environment.NORMAL).createWorld(); //create new world

        for(Player player : main.getServer().getOnlinePlayers()) {
            if(player.hasPermission("demianarchy.royale.avoid")) {
                player.sendMessage(main.prefix + ChatColor.GOLD + "You avoided the royale.");
                continue;
            }
            //teleport player to random location in world
            main.getRM().addPlayerLoc(player, player.getLocation());
            player.teleport(randomLocWithinBothRadius(royale, main.getConfig().getInt("royaleMaxRadius"), main.getConfig().getInt("royaleMinRadius")));
            player.sendMessage(main.prefix + ChatColor.BLUE + "The border will begin to shrink in " + ChatColor.GOLD + main.getConfig().getInt("royaleStartMins") + ChatColor.BLUE + " minutes.");
            players.add(player);
        }

        //setup border
        setupWorldBorder(royale, main.getConfig().getInt("royaleMaxRadius"), main.getConfig().getInt("royaleMinRadius"), main.getConfig().getInt("royaleShrinkMinutes") * 60);

        //Royale manager logic
        main.getRM().setRoyale(this);
    }

    //ends the battle royale
    public void finish() {
        if(players.isEmpty()) {
            Utils.messageAll(main.prefix + ChatColor.BLUE + "The royale has finished, but there were no victors.", main);
            return;
        }
        Utils.messageAll(main.prefix + ChatColor.BLUE + "The royale has finished, the victors are:", main);
        for(Player player : players) {
            Utils.messageAll(String.format(ChatColor.GREEN + "- " + ChatColor.GOLD + "%s", player.getName()), main);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() { //to make it run after other players get lives
            public void run() {
                for(Player player : players) {
                    players.remove(player);
                    main.getRM().teleportPlayerToPreviousLocation(player);
                }
            }
        }, 5 * 20L);
        main.getRM().resetRoyale();
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

    private Location randomLocWithinBothRadius(World world, int highRad, int lowRad) {

        //square coord
        double a = Math.random() * 2 * PI;
        double r = Math.sqrt(lowRad * lowRad + Math.random() * (highRad * highRad - lowRad * lowRad)) / Math.max(Math.abs(Math.cos(a)), Math.abs(Math.sin(a)));

        //cartesian coord
        int X = (int) (r * Math.sin(a));
        int Z = (int) (r * Math.cos(a));

        //build location
        Location ret = new Location(world, 0, 0,0);
        ret.setX(X);
        ret.setZ(Z);
        ret.setY(ret.getWorld().getHighestBlockYAt(ret.getBlockX(), ret.getBlockZ()));

        return ret;
    }

    private void setupWorldBorder(World world, int radius1, final int radius2, final int secondsFromTo) {
        final WorldBorder border = world.getWorldBorder();
        border.setCenter(0D, 0D);
        border.setSize((radius1 + 1) * 2);
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            public void run() {
                if(!main.getRM().isRoyale) {
                    return;
                }
                border.setSize(radius2, secondsFromTo);
                for(Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(main.prefix + ChatColor.BLUE + "The border is beginning to shrink!");
                }
            }
        }, 60L * 20 * main.getConfig().getInt("royaleStartMins"));

    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    //TODO: Shrinking border, when border hits minimum radius, make it move around in random directions to keep players mobile
    //TODO: Give players compass that locks onto player with most kills
    //TODO: If die in royale go back to where they were beforehand
    //TODO: Set everyones lives
}
