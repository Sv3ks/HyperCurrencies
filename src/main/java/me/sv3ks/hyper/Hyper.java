package me.sv3ks.hyper;

import org.bukkit.plugin.java.JavaPlugin;

public final class Hyper extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Hyper was enabled");

    }

    @Override
    public void onDisable() {
        this.getLogger().info("Hyper was disabled");
    }
}
