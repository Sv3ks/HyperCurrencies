package me.sv3ks.hypercurrencies;

import me.sv3ks.hypercurrencies.commands.HyperCurrenciesCommand;
import me.sv3ks.hypercurrencies.utils.Config;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyperCurrencies extends JavaPlugin {

    private static Plugin plugin;
    private static Config currencyConfig;
    private static Config dataConfig;

    @Override
    public void onEnable() {

        plugin = this;
        currencyConfig = new Config("currencies.yml");
        dataConfig = new Config("data.yml");

        currencyConfig.createConfig();
        dataConfig.createConfig();

        this.getCommand("hypercurrencies").setExecutor(new HyperCurrenciesCommand());

        this.getLogger().info("Hyper was enabled");

    }

    @Override
    public void onDisable() {
        this.getLogger().info("Hyper was disabled");
    }

    public static Plugin getPlugin() { return plugin; }
    public static Config getCurrencyConfig() { return currencyConfig; }
    public static Config getDataConfig() { return dataConfig; }

}
