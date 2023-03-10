package me.sv3ks.hypercurrencies.utils;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getDataConfig;

public class Currency {
    /*
    private final String name;

    public Currency(String name) {
        this.name = name;
    }

    public long getBalance(UUID player) {
        return getDataConfig().getConfig().getLong(name+"."+player);
    }

    public void addBalance(UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, getBalance(player)+amount);
    }

    public void removeBalance(UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, getBalance(player)-amount);
    }

    public void setBalance(UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, amount);
    }

    public String getName() { return name; }
     */

    public static long getBalance(String name, UUID player) {
        return getDataConfig().getConfig().getLong(name+"."+player);
    }

    public static void addBalance(String name, UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, getBalance(name, player)+amount);
        saveCurrencies();
    }

    public static void removeBalance(String name, UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, getBalance(name, player)-amount);
        saveCurrencies();
    }

    public static void setBalance(String name, UUID player, long amount) {
        getDataConfig().getConfig().set(name+"."+player, amount);
        saveCurrencies();
    }

    public static void createCurrency(String name) {
        getDataConfig().getConfig().set(name,"");
        saveCurrencies();
    }

    public static void deleteCurrency(String name) {
        getDataConfig().getConfig().set(name,null);
        saveCurrencies();
    }

    public static void saveCurrencies() {
        getDataConfig().saveConfig();
    }

    public static boolean currencyExists(String name) {
        return getDataConfig().getConfig().contains(name);
    }
}
