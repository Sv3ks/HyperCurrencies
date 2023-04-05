package me.sv3ks.hypercurrencies;

import me.sv3ks.hypercurrencies.commands.corecommands.CurrencyCommand;
import me.sv3ks.hypercurrencies.commands.corecommands.HyperCurrenciesCommand;
import me.sv3ks.hypercurrencies.commands.playercommands.BalanceCommand;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;
import me.sv3ks.hypercurrencies.currencies.providers.DefaultProvider;
import me.sv3ks.hypercurrencies.currencies.providers.SQLProvider;
import me.sv3ks.hypercurrencies.currencies.providers.VaultProvider;
import me.sv3ks.hypercurrencies.utils.Config;
import me.sv3ks.hypercurrencies.utils.UpdateChecker;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class HyperCurrencies extends JavaPlugin {

    private static Plugin plugin;
    private static Config currencyConfig;
    private static Config dataConfig;
    private static HashMap<String,CurrencyProvider> providers;

    @Override
    public void onEnable() {

        plugin = this;
        currencyConfig = new Config("currencies.yml");
        dataConfig = new Config("data.yml");
        providers = new HashMap<>();

        currencyConfig.createConfig();
        dataConfig.createConfig();
        getConfig().options().copyDefaults();
        currencyConfig.reloadConfig();
        dataConfig.reloadConfig();
        saveDefaultConfig();

        addProvider(new DefaultProvider());
        addProvider(new SQLProvider());
        if (getServer().getPluginManager().getPlugin("Vault")!=null) addProvider(new VaultProvider());

        // Core Commands
        this.getCommand("hypercurrencies").setExecutor(new HyperCurrenciesCommand());
        this.getCommand("currency").setExecutor(new CurrencyCommand());

        // Player Commands
        this.getCommand("balance").setExecutor(new BalanceCommand());

        if (getConfig().getBoolean("check-for-updates")) {
            new UpdateChecker(this, 108601).getVersion(version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    getLogger().info("HyperCurrencies is up to date.");
                } else {
                    getLogger().info("There is a new update available for HyperCurrencies.");
                }
            });
        }

        this.getLogger().info("Hyper was enabled");

    }

    @Override
    public void onDisable() {
        this.getLogger().info("Hyper was disabled");
    }

    public static Plugin getPlugin() { return plugin; }
    public static Config getCurrencyConfig() { return currencyConfig; }
    public static Config getDataConfig() { return dataConfig; }
    public static HashMap<String, CurrencyProvider> getProviders() {
        return providers;
    }

    public static void addProvider(CurrencyProvider provider) {
        providers.put(provider.getProviderID(),provider);
    }

}
