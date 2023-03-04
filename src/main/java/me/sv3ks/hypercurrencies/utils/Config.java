package me.sv3ks.hypercurrencies.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getPlugin;

public class Config {

    private final String fileName;
    private File configFile;
    private FileConfiguration config;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
            //YamlConfiguration.loadConfiguration(dataConfigFile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().info("Error while saving data config:");
            Bukkit.getServer().getLogger().info(e.toString());
        }
    }

    public void createConfig() {
        configFile = new File(getPlugin().getDataFolder(), fileName);
        if (!configFile.exists()) {
            if (!configFile.getParentFile().mkdirs()) getPlugin().getLogger().severe(ChatColor.DARK_RED + "Failed to create necessary directories.");
            // dataConfigFile.getParentFile().mkdirs();
            getPlugin().saveResource(fileName, false);
        }

        config = new YamlConfiguration();

        YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadConfig() {
        if (!configFile.exists()) {
            if (!configFile.getParentFile().mkdirs()) getPlugin().getLogger().severe(ChatColor.DARK_RED + "Failed to create necessary directories.");
            // dataConfigFile.getParentFile().mkdirs();
            getPlugin().saveResource(fileName, false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
