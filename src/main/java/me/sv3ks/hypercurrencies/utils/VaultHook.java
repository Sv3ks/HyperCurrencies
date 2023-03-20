package me.sv3ks.hypercurrencies.utils;

import net.milkbowl.vault.economy.Economy;

import static org.bukkit.Bukkit.getServer;

public class VaultHook {
    private static final Economy economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    public static Economy getEconomy() {
        return economy;
    }
}
