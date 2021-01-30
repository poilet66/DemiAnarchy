package me.poilet66.demianarchy;

import me.poilet66.demianarchy.Commands.LivesCommandClass;
import me.poilet66.demianarchy.Listeners.ListenerClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DemiAnarchy extends JavaPlugin {

    private PlayerLivesManager PLM;
    private TimeHandler TH;

    @Override
    public void onEnable() {
        loadConfig();
        this.PLM = new PlayerLivesManager(this);
        getServer().getPluginManager().registerEvents(new ListenerClass(this), this);
        getCommand("lives").setExecutor(new LivesCommandClass(this));
        this.TH = new TimeHandler(this);
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        PLM.save(PLM.getLivesMap(), new File(getDataFolder(), "livesMap.dat"));
        PLM.save(PLM.getDeadPlayersLocMap(), new File(getDataFolder(), "deadPlayers.dat"));
    }

    public PlayerLivesManager getPLM() {
        return PLM;
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
