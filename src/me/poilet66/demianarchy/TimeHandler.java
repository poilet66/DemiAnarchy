package me.poilet66.demianarchy;

import me.poilet66.demianarchy.Util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TimeHandler {

    private final DemiAnarchy main;

    public TimeHandler(DemiAnarchy main) {
        this.main = main;
        try {
            setupDeadPlayerRepeatingTask();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDeadPlayerRepeatingTask() {
        main.getLogger().info(String.format("Starting timer task every %d seconds", main.getConfig().getLong("timerRepeat")));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            public void run() {
                for(UUID playerUUID : main.getPLM().getDeadPlayersLocMap().keySet()) {
                    Player player = main.getServer().getPlayer(playerUUID);
                    if(player != null) { //if online
                        if(Math.abs(Utils.getPointDistanceFromPlayer(player, main.getPLM().getDeadPlayersLocMap().get(playerUUID))) >= main.getConfig().getDouble("deathRadius")) {
                            player.teleport(main.getPLM().getDeadPlayersLocMap().get(playerUUID));
                            player.sendMessage(ChatColor.RED + "You strayed too far from your death point.");
                        }
                    }
                }
            }
        }, 0L, 20L * main.getConfig().getLong("timerRepeat"));
    }
}

