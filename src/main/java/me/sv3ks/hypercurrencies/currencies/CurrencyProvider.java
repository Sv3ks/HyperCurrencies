package me.sv3ks.hypercurrencies.currencies;

import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

public abstract class CurrencyProvider {

    public abstract String getProviderID();

    public abstract boolean change(ChangeType type, String name, UUID uuid, double amount);

    public abstract double get(String name, UUID uuid);

    /**
     * @param name The name of the currency
     * @return A map containing the balance top from 1-100
     */
    public abstract Map<Integer,UUID> getBalanceTop(String name);
}
