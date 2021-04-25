package me.poilet66.demianarchy;

import me.poilet66.demianarchy.Objects.Royale;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RoyaleManager {

    private final DemiAnarchy main;
    private HashMap<UUID, Location> playerLocationMap = new HashMap<>();

    private Royale currentRoyale;
    public boolean isRoyale;

    public RoyaleManager(DemiAnarchy main) {
        this.main = main;
        this.isRoyale = false;
    }

    public void setRoyale(Royale royale) {
        this.currentRoyale = royale;
        isRoyale = true;
    }

    public Royale getRoyale() {
        return currentRoyale;
    }

    public void resetRoyale() {
        this.currentRoyale = null;
        isRoyale = false;
    }

    public void addPlayerLoc(Player player, Location loc) {
        if(!playerLocationMap.containsKey(player.getUniqueId())) {
            playerLocationMap.put(player.getUniqueId(), loc);
        }
    }

    public void teleportPlayerToPreviousLocation(Player player) {
        if(playerLocationMap.containsKey(player.getUniqueId())) {
            player.teleport(playerLocationMap.get(player.getUniqueId()));
            playerLocationMap.remove(player.getUniqueId());
        }
    }
}
