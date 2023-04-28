package me.sv3ks.hypercurrencies;

import me.sv3ks.hypercurrencies.commands.corecommands.CurrencyCommand;
import me.sv3ks.hypercurrencies.commands.corecommands.HyperCurrenciesCommand;
import me.sv3ks.hypercurrencies.commands.playercommands.BalanceCommand;
import me.sv3ks.hypercurrencies.commands.playercommands.BalancetopCommand;
import me.sv3ks.hypercurrencies.commands.playercommands.PayCommand;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;
import me.sv3ks.hypercurrencies.currencies.providers.DefaultProvider;
import me.sv3ks.hypercurrencies.currencies.providers.SQLProvider;
import me.sv3ks.hypercurrencies.currencies.providers.VaultProvider;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.Placeholder;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.PlaceholderAPIHook;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders.BalanceOtherPlaceholder;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders.BalancePlaceholder;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders.BaltopPlaceholder;
import me.sv3ks.hypercurrencies.utils.Config;
import me.sv3ks.hypercurrencies.utils.Metrics;
import me.sv3ks.hypercurrencies.utils.UpdateChecker;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class HyperCurrencies extends JavaPlugin {

    private static Plugin plugin;
    private static Config currencyConfig;
    private static Config dataConfig;
    private static HashMap<String,CurrencyProvider> providers;
    private static Metrics metrics;
    private static List<Placeholder> placeholders;

    @Override
    public void onEnable() {

        plugin = this;
        currencyConfig = new Config("currencies.yml");
        dataConfig = new Config("data.yml");
        providers = new HashMap<>();
        placeholders = new ArrayList<>();

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
        this.getCommand("balancetop").setExecutor(new BalancetopCommand());
        this.getCommand("pay").setExecutor(new PayCommand());

        if (getConfig().getBoolean("check-for-updates")) {
            new UpdateChecker(this, 108601).getVersion(version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    getLogger().info("HyperCurrencies is up to date.");
                } else {
                    getLogger().info("There is a new update available for HyperCurrencies.");
                }
            });
        }

        // PlaceholderAPI registration
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI")!=null) new PlaceholderAPIHook().register();

        // Save default languages
        saveResource("/lang/lang_en-us.yml", false);
        saveResource("/lang/lang_da-dk.yml", false);
        saveResource("/lang/lang_zh-cn.yml", false);

        // Load bStats Metrics
        metrics = new Metrics(this,18221);

        // Registering Placeholders
        addPlaceholder(new BalancePlaceholder());
        addPlaceholder(new BalanceOtherPlaceholder());
        addPlaceholder(new BaltopPlaceholder());

        this.getLogger().info("HyperCurrencies was enabled");

    }

    @Override
    public void onDisable() {
        this.getLogger().info("HyperCurrencies was disabled");
    }

    public static Plugin getPlugin() { return plugin; }
    public static Config getCurrencyConfig() { return currencyConfig; }
    public static Config getDataConfig() { return dataConfig; }
    public static HashMap<String, CurrencyProvider> getProviders() {
        return providers;
    }
    public static void addMetricsCurrencyChartValue() {
        metrics.addCustomChart(new Metrics.SingleLineChart("total_currencies", () -> 1));
    }

    public static void addProvider(CurrencyProvider provider) {
        providers.put(provider.getProviderID(),provider);
    }

    public static void addPlaceholder(Placeholder placeholder) {
        placeholders.add(placeholder);
    }

    public static List<Placeholder> getPlaceholders() {
        return placeholders;
    }

}
