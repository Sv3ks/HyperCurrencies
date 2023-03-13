package me.sv3ks.hypercurrencies.currencies;

import java.util.UUID;

public abstract class CurrencyProvider {

    protected String providerID;

    public String getProviderID() {
        return providerID;
    }

    public abstract boolean change(ChangeType type, UUID uuid, long amount);

    public abstract long get(UUID uuid);

}
