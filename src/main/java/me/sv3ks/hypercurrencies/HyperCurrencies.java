package me.sv3ks.hypercurrencies;

import me.sv3ks.hypercurrencies.commands.CurrencyCommand;
import me.sv3ks.hypercurrencies.commands.HyperCurrenciesCommand;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;
import me.sv3ks.hypercurrencies.currencies.providers.DefaultProvider;
import me.sv3ks.hypercurrencies.currencies.providers.VaultProvider;
import me.sv3ks.hypercurrencies.utils.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class HyperCurrencies extends JavaPlugin {

    private static Plugin plugin;
    private static Config currencyConfig;
    private static Config dataConfig;
    private static Economy economy;
    private static HashMap<String,CurrencyProvider> providers;

    @Override
    public void onEnable() {

        plugin = this;
        currencyConfig = new Config("currencies.yml");
        dataConfig = new Config("data.yml");
        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        providers = new HashMap<>();

        currencyConfig.createConfig();
        dataConfig.createConfig();

        addProvider(new DefaultProvider());
        addProvider(new VaultProvider());

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
    public static HashMap<String, CurrencyProvider> getProviders() {
        return providers;
    }

    public static void addProvider(CurrencyProvider provider) {
        providers.put(provider.getProviderID(),provider);
    }

}
