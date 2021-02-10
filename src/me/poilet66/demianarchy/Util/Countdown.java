package me.poilet66.demianarchy.Util;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Countdown {

    private int time;
    private int delay;

    protected BukkitTask task;
    protected final Plugin plugin;


    public Countdown(int time, int delay, Plugin plugin) {
        this.time = time;
        this.plugin = plugin;
        this.delay = delay;
    }


    public abstract void count(int current);

    public abstract void finish();


    public final void start() {
        task = new BukkitRunnable() {

            public void run() {
                if(time-- <= 0) {
                    finish();
                    task.cancel();
                }
                else{
                    count(time);
                }
            }

        }.runTaskTimer(plugin, 0L, 20L * delay);
    }

}
