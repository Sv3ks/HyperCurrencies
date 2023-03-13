package me.sv3ks.hypercurrencies;

import me.hsgamer.bettereconomy.BetterEconomy;
import me.sv3ks.hypercurrencies.commands.CurrencyCommand;
import me.sv3ks.hypercurrencies.commands.HyperCurrenciesCommand;
import me.sv3ks.hypercurrencies.utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HyperCurrencies extends JavaPlugin {

    private static Plugin plugin;
    private static Config currencyConfig;
    private static Config dataConfig;
    private static Economy economy;

    @Override
    public void onEnable() {

        plugin = this;
        currencyConfig = new Config("currencies.yml");
        dataConfig = new Config("data.yml");

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        currencyConfig.createConfig();
        dataConfig.createConfig();

        this.getCommand("hypercurrencies").setExecutor(new HyperCurrenciesCommand());
        this.getCommand("currency").setExecutor(new CurrencyCommand());

        this.getLogger().info("Hyper was enabled");



    }

    @Override
    public void onDisable() {
        this.getLogger().info("Hyper was disabled");
    }

    public static Plugin getPlugin() { return plugin; }
    public static Config getCurrencyConfig() { return currencyConfig; }
    public static Config getDataConfig() { return dataConfig; }
    public static Economy getEconomy() {
        return economy;
    }

}
