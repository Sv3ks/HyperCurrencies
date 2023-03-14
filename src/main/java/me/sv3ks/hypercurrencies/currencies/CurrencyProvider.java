package me.sv3ks.hypercurrencies.currencies;

import java.util.UUID;

public abstract class CurrencyProvider {

    protected String providerID;

    public String getProviderID() {
        return providerID;
    }

    public abstract boolean change(ChangeType type, String name, UUID uuid, double amount);

    public abstract double get(String name, UUID uuid);

}
