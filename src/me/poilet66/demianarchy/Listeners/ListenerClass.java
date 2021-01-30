package me.poilet66.demianarchy.Listeners;

import me.poilet66.demianarchy.DemiAnarchy;
import me.poilet66.demianarchy.PlayerLivesManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ListenerClass implements Listener {

    private final DemiAnarchy main;
    private final PlayerLivesManager PLM;

    public ListenerClass(DemiAnarchy main) {
        this.main = main;
        this.PLM = main.getPLM();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(PLM.decrementPlayerLife(event.getEntity().getUniqueId())) {
            event.getEntity().sendMessage(ChatColor.RED + "You have no more lives remaining.");
            event.getEntity().teleport(PLM.getDeadPlayersLocMap().get(event.getEntity().getUniqueId()));
            event.getEntity().setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!PLM.getLivesMap().containsKey(event.getPlayer().getUniqueId())) {
            PLM.addPlayer(event.getPlayer().getUniqueId());
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
