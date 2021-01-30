package me.poilet66.demianarchy;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DemiAnarchy extends JavaPlugin {

    private PlayerLivesManager PLM;

    @Override
    public void onEnable() {
        loadConfig();
        this.PLM = new PlayerLivesManager(this);
        getServer().getPluginManager().registerEvents(new ListenerClass(this), this);
        getCommand("lives").setExecutor(new LivesCommandClass(this));
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        PLM.save(PLM.getLivesMap(), new File(getDataFolder(), "livesMap.dat"));
    }

    public PlayerLivesManager getPLM() {
        return PLM;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
