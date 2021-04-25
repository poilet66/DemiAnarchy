package me.poilet66.demianarchy;

import me.poilet66.demianarchy.Commands.LivesCommand;
import me.poilet66.demianarchy.Commands.LivesCommandTabComplete;
import me.poilet66.demianarchy.Commands.RoyaleCommand;
import me.poilet66.demianarchy.Commands.RoyaleCommandTabComplete;
import me.poilet66.demianarchy.Listeners.ListenerClass;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DemiAnarchy extends JavaPlugin {

    public final static String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "DemiAnarchy" + ChatColor.GRAY + "] ";

    private PlayerLivesManager PLM;
    private RoyaleManager RM;
    private TimeHandler TH;
    
    @Override
    public void onEnable() {
        loadConfig();
        this.PLM = new PlayerLivesManager(this);
        this.RM = new RoyaleManager(this);
        this.TH = new TimeHandler(this);
        getServer().getPluginManager().registerEvents(new ListenerClass(this), this);
        getCommand("lives").setExecutor(new LivesCommand(this));
        getCommand("royale").setExecutor(new RoyaleCommand(this));
        getCommand("lives").setTabCompleter(new LivesCommandTabComplete());
        getCommand("royale").setTabCompleter(new RoyaleCommandTabComplete());
        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        PLM.save(PLM.getLivesMap(), new File(getDataFolder(), "livesMap.dat"));
        PLM.prepareDeadPlayerSave();
        PLM.save(PLM.getDeadPlayers(), new File(getDataFolder(), "deadPlayers.dat"));
    }

    public PlayerLivesManager getPLM() {
        return PLM;
    }

    public TimeHandler getTH() {
        return TH;
    }

    public RoyaleManager getRM() {
        return RM;
    }

    public void loadConfig() {
        saveDefaultConfig();
    }
}
