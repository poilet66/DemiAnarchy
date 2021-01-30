package me.poilet66.demianarchy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ListenerClass implements Listener {

    private DemiAnarchy main;
    private PlayerLivesManager PLM;

    public ListenerClass(DemiAnarchy main) {
        this.main = main;
        this.PLM = main.getPLM();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PLM.decrementPlayerLife(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!PLM.getLivesMap().containsKey(event.getPlayer().getUniqueId())) {
            PLM.addPlayer(event.getPlayer().getUniqueId());
        }
    }
}
