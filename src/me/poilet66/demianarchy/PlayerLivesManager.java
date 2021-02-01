package me.poilet66.demianarchy;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerLivesManager {

    private HashMap<UUID, Integer> livesMap;
    private Set<UUID> deadPlayers;
    private HashMap<UUID, Location> deadPlayersLocMap;
    private final Plugin main;

    public PlayerLivesManager(Plugin main) {
        this.main = main;
        File dir = main.getDataFolder();
        if(!dir.exists()) {
            if(!dir.mkdir()) {
                main.getLogger().info("Couldn't create directory for data folder");
            }
        }
        livesMap = (HashMap<UUID, Integer>) load(new File(main.getDataFolder(), "livesMap.dat"));
        if(livesMap == null) {
            livesMap = new HashMap<UUID, Integer>();
        }
        deadPlayers = (Set<UUID>) load(new File(main.getDataFolder(), "deadPlayers.dat"));
        if(deadPlayers == null) {
            deadPlayers = new HashSet<UUID>();
        }
        deadPlayersLocMap = new HashMap<UUID, Location>();
    }


    /**
     *
     * @param player
     * @return true if player has no more lives
     */
    public boolean decrementPlayerLife(UUID player) {
        if(livesMap.containsKey(player)) {
            if(livesMap.get(player) > 1) {
                int lives = livesMap.get(player);
                livesMap.put(player, lives-1);
                return false;
            }
            else if(livesMap.get(player) == 1) {
                int lives = livesMap.get(player);
                livesMap.put(player, lives-1);
                deadPlayersLocMap.put(player, main.getServer().getPlayer(player).getLocation());
            }
            //if have 0 lives will just skip to here, will still tp back and stuff just wont decrement life or set a new dead location
            return true;
        }
        return false;
    }

    public void addPlayerLife(UUID player) {
        if(livesMap.containsKey(player)) {
            int lives = livesMap.get(player);
            livesMap.put(player, lives+1);
        }
    }

    public void setPlayerLife(UUID player, int amount) {
        if(livesMap.containsKey(player)) {
            if(amount > 0 && deadPlayersLocMap.containsKey(player)) {
                deadPlayersLocMap.remove(player);
                main.getServer().getPlayer(player).setGameMode(GameMode.SURVIVAL);
            }
            livesMap.put(player, amount);
        }
    }

    public void addPlayer(UUID player) {
        livesMap.put(player, main.getConfig().getInt("defaultLives"));
    }

    public HashMap<UUID, Integer> getLivesMap() {
        return livesMap;
    }

    public HashMap<UUID, Location> getDeadPlayersLocMap() { return deadPlayersLocMap; }

    public Set<UUID> getDeadPlayers() {
        return deadPlayers;
    }

    public void prepareDeadPlayerSave() {
        for(UUID player : deadPlayersLocMap.keySet()) {
            deadPlayers.add(player);
        }
    }

    public void save(Object o, File f) {
        try{
            if(!f.exists()) {
                f.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(o);
            oos.flush();
            oos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Object load(File f) {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object result = ois.readObject();
            ois.close();
            return result;
        } catch(Exception e) {
            return null;
        }
    }
}
