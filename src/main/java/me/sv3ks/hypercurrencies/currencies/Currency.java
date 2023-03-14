package me.sv3ks.hypercurrencies.currencies;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;

public class Currency {

    private final String name;
    private final String provider;
    private final double startingBal;
    private final double minBal;
    private final double maxBal;

    public Currency(String name) {
        if (getCurrencyConfig().getConfig().get(name)==null) createCurrency(name);

        this.name = name;

        // Starting bal
        if (getCurrencyConfig().getConfig().get("starting-bal")==null) this.startingBal = 0;
        else this.startingBal = getCurrencyConfig().getConfig().getDouble("starting-bal");

        // Min bal
        if (getCurrencyConfig().getConfig().get("min-bal")==null) this.minBal = 0;
        else this.minBal = getCurrencyConfig().getConfig().getDouble("starting-bal");

        // Max bal
        if (getCurrencyConfig().getConfig().get("max-bal")==null) this.maxBal = 0;
        else this.maxBal = getCurrencyConfig().getConfig().getDouble("max-bal");

        // Provider
        if (getCurrencyConfig().getConfig().get("provider")==null) this.provider = "hypercurrencies";
        else this.provider = getCurrencyConfig().getConfig().getString("provider");
    }

    public boolean addBalance(UUID player, double amount) {
        return addBalance(name,player,amount);
    }

    public boolean removeBalance(UUID player, double amount) {
        return removeBalance(name,player,amount);
    }

    public boolean setBalance(UUID player, double amount) {
        return setBalance(name,player,amount);
    }

    // Getters

    public double getBalance(UUID player) {
        return getBalance(name,player);
    }

    public String getName() {
        return name;
    }

    public String getProvider() {
        return provider;
    }

    public double getStartingBal() {
        return startingBal;
    }

    public double getMinBal() {
        return minBal;
    }

    public double getMaxBal() {
        return maxBal;
    }

    public boolean hasBalance(UUID player) {
        return hasBalance(name,player);
    }

    /* Statics */

    public static double getBalance(String name, UUID player) {
        return getProviders().get(name).get(name,player);
    }

    public static boolean addBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.ADD,name,player,amount);
    }

    public static boolean removeBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.REMOVE,name,player,amount);
    }

    public static boolean setBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.SET,name,player,amount);
    }

    public static void createCurrency(String name) {
        getCurrencyConfig().getConfig().set(name+".starting-bal",0);
        getCurrencyConfig().getConfig().set(name+".min-bal",0);
        getCurrencyConfig().getConfig().set(name+".max-bal",1000000000000000000d);
        getCurrencyConfig().getConfig().set(name+".provider","hypercurrencies");
        saveCurrencies();
    }

    public static void deleteCurrency(String name) {
        getDataConfig().getConfig().set(name,null);
        getCurrencyConfig().getConfig().set(name,null);
        saveCurrencies();
    }

    public static void saveCurrencies() {
        getDataConfig().saveConfig();
        getCurrencyConfig().saveConfig();
    }

    public static boolean currencyExists(String name) {
        return getDataConfig().getConfig().contains(name);
    }

    public static boolean hasBalance(String name, UUID player) {
        return getDataConfig().getConfig().get(name+"."+player)!=null;
    }
}
