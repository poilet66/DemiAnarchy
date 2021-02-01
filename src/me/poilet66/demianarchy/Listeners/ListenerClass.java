package me.poilet66.demianarchy.Listeners;

import me.poilet66.demianarchy.DemiAnarchy;
import me.poilet66.demianarchy.PlayerLivesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class ListenerClass implements Listener {

    private final DemiAnarchy main;
    private final PlayerLivesManager PLM;

    public ListenerClass(DemiAnarchy main) {
        this.main = main;
        this.PLM = main.getPLM();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void playerKillPlayer(PlayerDeathEvent event) {
        if(event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();
            if(!PLM.getLivesMap().containsKey(killer.getUniqueId()) || !PLM.getLivesMap().containsKey(victim.getUniqueId())) {
                killer.sendMessage(ChatColor.RED + "You weren't able to steal a life as an error occurred.");
                return;
            }
            if(PLM.getLivesMap().get(victim.getUniqueId()) < 1) {
                killer.sendMessage(ChatColor.RED + "Your victim had no lives to steal!");
                return;
            }
            killer.sendMessage(ChatColor.GREEN + "You stole a life from your victim.");
            PLM.getLivesMap().put(killer.getUniqueId(), PLM.getLivesMap().get(killer.getUniqueId())+1);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() { //to make it run after other players get lives
            public void run() {
                if(PLM.decrementPlayerLife(event.getEntity().getUniqueId())) {
                    event.getEntity().sendMessage(ChatColor.RED + "You have no more lives remaining.");
                    event.getEntity().teleport(PLM.getDeadPlayersLocMap().get(event.getEntity().getUniqueId()));
                    event.getEntity().setGameMode(GameMode.ADVENTURE);
                }
            }
        }, 2L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!PLM.getLivesMap().containsKey(event.getPlayer().getUniqueId())) {
            PLM.addPlayer(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeadPlayerJoin(PlayerJoinEvent event) {
        if(PLM.getDeadPlayers().contains(event.getPlayer().getUniqueId())) {
            PLM.getDeadPlayersLocMap().put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
        }
    }

    @EventHandler
    public void onDeadPlayerLeave(PlayerQuitEvent event) {
        if(PLM.getDeadPlayersLocMap().containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().teleport(PLM.getDeadPlayersLocMap().get(event.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(PLM.getDeadPlayersLocMap().containsKey(event.getPlayer().getUniqueId())) {
            event.setRespawnLocation(PLM.getDeadPlayersLocMap().get(event.getPlayer().getUniqueId()));
            event.getPlayer().sendMessage("Teleporting back to death point");
        }
    }

}
