package me.poilet66.demianarchy;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerLivesManager {

    private HashMap<UUID, Integer> livesMap;
    private Plugin main;

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
    }

    public void decrementPlayerLife(UUID player) {
        if(livesMap.containsKey(player)) {
            if(livesMap.get(player) > 0) {
                int lives = livesMap.get(player);
                livesMap.put(player, lives-1);
            }
        }
    }

    public void addPlayerLife(UUID player) {
        if(livesMap.containsKey(player)) {
            int lives = livesMap.get(player);
            livesMap.put(player, lives+1);
        }
    }

    public void setPlayerLife(UUID player, int amount) {
        if(livesMap.containsKey(player)) {
            livesMap.put(player, amount);
        }
    }

    public void addPlayer(UUID player) {
        livesMap.put(player, main.getConfig().getInt("defaultLives"));
    }

    public HashMap<UUID, Integer> getLivesMap() {
        return livesMap;
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
