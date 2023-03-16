package me.sv3ks.hypercurrencies.currencies;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;

public class Currency {

    private final String name;
    private CurrencyProvider provider;
    private double startingBal;
    private double minBal;
    private double maxBal;

    public Currency(String name) {
        if (getCurrencyConfig().getConfig().get(name)==null) {
            getCurrencyConfig().getConfig().set(name+".starting-bal",0);
            getCurrencyConfig().getConfig().set(name+".min-bal",0);
            getCurrencyConfig().getConfig().set(name+".max-bal",1000000000000000000d);
            getCurrencyConfig().getConfig().set(name+".provider","hypercurrencies");
            getCurrencyConfig().saveConfig();
        }

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
        if (getCurrencyConfig().getConfig().get("provider")==null) this.provider = getProviders().get("hypercurrencies");
        else this.provider = getProviders().get(getCurrencyConfig().getConfig().getString("provider"));
    }

    public void delete() {
        getDataConfig().getConfig().set(name,null);
        getCurrencyConfig().getConfig().set(name,null);
        getDataConfig().saveConfig();
        getCurrencyConfig().saveConfig();
    }

    // Setters

    public boolean addBalance(UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.ADD,name,player,amount);
    }

    public boolean removeBalance(UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.REMOVE,name,player,amount);
    }

    public boolean setBalance(UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.SET,name,player,amount);
    }

    public void setProvider(CurrencyProvider provider) {
        this.provider = provider;
        getCurrencyConfig().getConfig().set(name+".provider",provider.getProviderID());
        getCurrencyConfig().saveConfig();
    }

    public void setStartingBal(double amount) {
        startingBal = amount;
        getCurrencyConfig().getConfig().set(name+".starting-bal",amount);
        getCurrencyConfig().saveConfig();
    }

    public void setMaxBal(double amount) {
        maxBal = amount;
        getCurrencyConfig().getConfig().set(name+".max-bal",amount);
        getCurrencyConfig().saveConfig();
    }

    public void setMinBal(double amount) {
        minBal = amount;
        getCurrencyConfig().getConfig().set(name+".min-bal",amount);
        getCurrencyConfig().saveConfig();
    }

    // Getters

    public double getBalance(UUID player) {
        return getProviders().get(name).get(name,player);
    }

    public String getName() {
        return name;
    }

    public CurrencyProvider getProvider() {
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

    @Deprecated
    public boolean hasBalance(UUID player) {
        return getDataConfig().getConfig().get(name+"."+player)!=null;
    }

    /* Statics */

    @Deprecated
    public static double getBalance(String name, UUID player) {
        return getProviders().get(name).get(name,player);
    }

    @Deprecated
    public static boolean addBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.ADD,name,player,amount);
    }

    @Deprecated
    public static boolean removeBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.REMOVE,name,player,amount);
    }

    @Deprecated
    public static boolean setBalance(String name, UUID player, double amount) {
        return getProviders().get(name).change(ChangeType.SET,name,player,amount);
    }

    @Deprecated
    public static void createCurrency(String name) {
        getCurrencyConfig().getConfig().set(name+".starting-bal",0);
        getCurrencyConfig().getConfig().set(name+".min-bal",0);
        getCurrencyConfig().getConfig().set(name+".max-bal",1000000000000000000d);
        getCurrencyConfig().getConfig().set(name+".provider","hypercurrencies");
        saveCurrencies();
    }

    @Deprecated
    public static void deleteCurrency(String name) {
        getDataConfig().getConfig().set(name,null);
        getCurrencyConfig().getConfig().set(name,null);
        saveCurrencies();
    }

    @Deprecated
    public static void saveCurrencies() {
        getDataConfig().saveConfig();
        getCurrencyConfig().saveConfig();
    }

    @Deprecated
    public static boolean currencyExists(String name) {
        return getDataConfig().getConfig().contains(name);
    }

    @Deprecated
    public static boolean hasBalance(String name, UUID player) {
        return getDataConfig().getConfig().get(name+"."+player)!=null;
    }
}
