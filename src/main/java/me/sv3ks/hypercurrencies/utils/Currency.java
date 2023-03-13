package me.sv3ks.hypercurrencies.utils;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;
import static org.bukkit.Bukkit.getPlayer;

public class Currency {

    private final String name;
    private final String provider;
    private final long startingBal;
    private final long minBal;
    private final long maxBal;

    public Currency(String name) {
        if (getCurrencyConfig().getConfig().get(name)==null) createCurrency(name);

        this.name = name;

        // Starting bal
        if (getCurrencyConfig().getConfig().get("starting-bal")==null) this.startingBal = 0;
        else this.startingBal = getCurrencyConfig().getConfig().getLong("starting-bal");

        // Min bal
        if (getCurrencyConfig().getConfig().get("min-bal")==null) this.minBal = 0;
        else this.minBal = getCurrencyConfig().getConfig().getLong("starting-bal");

        // Max bal
        if (getCurrencyConfig().getConfig().get("max-bal")==null) this.maxBal = 0;
        else this.maxBal = getCurrencyConfig().getConfig().getLong("max-bal");

        // Provider
        if (getCurrencyConfig().getConfig().get("provider")==null) this.provider = "hypercurrencies";
        else this.provider = getCurrencyConfig().getConfig().getString("provider");
    }

    public boolean addBalance(UUID player, long amount) {
        return addBalance(name,player,amount);
    }

    public boolean removeBalance(UUID player, long amount) {
        return removeBalance(name,player,amount);
    }

    public boolean setBalance(UUID player, long amount) {
        return setBalance(name,player,amount);
    }

    // Getters

    public long getBalance(UUID player) {
        return getBalance(name,player);
    }

    public String getName() {
        return name;
    }

    public String getProvier() {
        return provider;
    }

    public long getStartingBal() {
        return startingBal;
    }

    public long getMinBal() {
        return minBal;
    }

    public long getMaxBal() {
        return maxBal;
    }

    public boolean hasBalance(UUID player) {
        return hasBalance(name,player);
    }

    /* Statics */

    public static long getBalance(String name, UUID player) {

        if (new Currency(name).provider.equalsIgnoreCase("vault")) {
            return (long) getEconomy().getBalance(getPlayer(player));
        }

        if (!hasBalance(name,player)) setBalance(name,player,new Currency(name).getStartingBal());

        return getDataConfig().getConfig().getLong(name+"."+player);
    }

    public static boolean addBalance(String name, UUID player, long amount) {
        Currency currency = new Currency(name);

        if (
                amount+currency.getBalance(player)>currency.getMaxBal()
        ) {
            return false;
        }

        if (new Currency(name).provider.equalsIgnoreCase("vault")) {
            getEconomy().depositPlayer(getPlayer(player),amount);
            return true;
        }

        getDataConfig().getConfig().set(name+"."+player, getBalance(name, player)+amount);
        saveCurrencies();

        return true;
    }

    public static boolean removeBalance(String name, UUID player, long amount) {
        Currency currency = new Currency(name);

        if (
                currency.getBalance(player)-amount<currency.getMinBal()
        ) {
            return false;
        }

        if (new Currency(name).provider.equalsIgnoreCase("vault")) {
            getEconomy().withdrawPlayer(getPlayer(player),amount);
            return true;
        }

        getDataConfig().getConfig().set(name+"."+player, getBalance(name, player)-amount);
        saveCurrencies();

        return true;
    }

    public static boolean setBalance(String name, UUID player, long amount) {
        Currency currency = new Currency(name);

        if (
                currency.getBalance(player)-amount<currency.getMinBal()||
                amount+currency.getBalance(player)>currency.getMaxBal()
        ) {
            return false;
        }

        if (new Currency(name).provider.equalsIgnoreCase("vault")) {
            getEconomy().withdrawPlayer(getPlayer(player),currency.getBalance(player));
            getEconomy().depositPlayer(getPlayer(player),amount);
            return true;
        }

        getDataConfig().getConfig().set(name+"."+player, amount);
        saveCurrencies();

        return true;
    }

    public static void createCurrency(String name) {
        getCurrencyConfig().getConfig().set(name+".starting-bal",0);
        getCurrencyConfig().getConfig().set(name+".min-bal",0);
        getCurrencyConfig().getConfig().set(name+".max-bal",1000000000000000000L);
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
