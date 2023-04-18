package me.sv3ks.hypercurrencies.currencies;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;

public class Currency {

    private final String name;
    private CurrencyProvider provider;
    private double startingBal;
    private double minBal;
    private double maxBal;
    private boolean payState;
    private double payMin;

    public Currency(String name) {
        if (getCurrencyConfig().getConfig().get(name)==null) {
            getCurrencyConfig().getConfig().set(name+".starting-bal",0);
            getCurrencyConfig().getConfig().set(name+".min-bal",0);
            getCurrencyConfig().getConfig().set(name+".max-bal",1000000000000000000d);
            getCurrencyConfig().getConfig().set(name+".provider","hypercurrencies");
            getCurrencyConfig().saveConfig();
            addMetricsCurrencyChartValue();
        }

        this.name = name;

        // Starting bal
        if (getCurrencyConfig().getConfig().get(name+".starting-bal")==null) this.startingBal = 0;
        else this.startingBal = getCurrencyConfig().getConfig().getDouble(name+".starting-bal");

        // Min bal
        if (getCurrencyConfig().getConfig().get(name+".min-bal")==null) this.minBal = 0;
        else this.minBal = getCurrencyConfig().getConfig().getDouble(name+".min-bal");

        // Max bal
        if (getCurrencyConfig().getConfig().get(name+".max-bal")==null) this.maxBal = 1000000000000000000d;
        else this.maxBal = getCurrencyConfig().getConfig().getDouble(name+".max-bal");

        // Provider
        if (getCurrencyConfig().getConfig().get(name+".provider")==null) this.provider = getProviders().get("hypercurrencies");
        else this.provider = getProviders().get(getCurrencyConfig().getConfig().getString(name+".provider"));

        // Pay
        if (getCurrencyConfig().getConfig().get(name+".pay")==null) this.payState = true;
        else this.payState = getCurrencyConfig().getConfig().getBoolean(name+".pay");

        // Pay Min
        if (getCurrencyConfig().getConfig().get(name+".pay-min")==null) this.payMin = 1;
        else this.payMin = getCurrencyConfig().getConfig().getDouble(name+".pay-min");

        getCurrencyConfig().saveConfig();
    }

    public void delete() {
        getDataConfig().getConfig().set(name,null);
        getCurrencyConfig().getConfig().set(name,null);
        getDataConfig().saveConfig();
        getCurrencyConfig().saveConfig();
    }

    // Setters

    public boolean addBalance(UUID player, double amount) {
        return getProviders().get(getProvider().getProviderID()).change(ChangeType.ADD,name,player,amount);
    }

    public boolean removeBalance(UUID player, double amount) {
        return getProviders().get(getProvider().getProviderID()).change(ChangeType.REMOVE,name,player,amount);
    }

    public boolean setBalance(UUID player, double amount) {
        return getProviders().get(getProvider().getProviderID()).change(ChangeType.SET,name,player,amount);
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

    public void setPayState(boolean state) {
        payState = state;
        getCurrencyConfig().getConfig().set(name+".pay",state);
        getCurrencyConfig().saveConfig();
    }

    public void setPayMin(double amount) {
        payMin = amount;
        getCurrencyConfig().getConfig().set(name+".pay-min",amount);
        getCurrencyConfig().saveConfig();
    }

    public void togglePay(UUID uuid) {
        if (getPayToggleState(uuid)) getDataConfig().getConfig().set("pay-toggle."+uuid,false);
        else getDataConfig().getConfig().set("pay-toggle."+uuid,true);
        getDataConfig().saveConfig();
    }

    // Getters

    public double getBalance(UUID player) {
        return getProviders().get(getProvider().getProviderID()).get(name,player);
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

    public boolean getPayState() {
        return payState;
    }

    public double getPayMin() {
        return payMin;
    }

    public boolean getPayToggleState(UUID uuid) {
        if (getDataConfig().getConfig().getString("pay-toggle."+uuid)==null) {
            getDataConfig().getConfig().set("pay-toggle."+uuid,true);
            getDataConfig().saveConfig();
        }
        return getDataConfig().getConfig().getBoolean("pay-toggle."+uuid);
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
    public static boolean hasBalance(String name, UUID player) {
        return getDataConfig().getConfig().get(name+"."+player)!=null;
    }

    public static boolean currencyExists(String name) {
        return getCurrencyConfig().getConfig().getKeys(false).contains(name);
    }
}
