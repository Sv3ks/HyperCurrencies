package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getEconomy;
import static org.bukkit.Bukkit.getPlayer;

public class VaultProvider extends CurrencyProvider {

    public VaultProvider() {
        providerID = "vault";
    }

    @Override
    public boolean change(ChangeType type, UUID uuid, long amount) {
        switch (type) {
            case ADD:
                getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
            case REMOVE:
                getEconomy().withdrawPlayer(getPlayer(uuid),amount);
                break;
            case SET:
                // Player balance -> 0
                getEconomy().withdrawPlayer(getPlayer(uuid),get(uuid));
                // Player balance -> amount
                getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
        }

        return true;
    }

    @Override
    public long get(UUID uuid) {
        return (long) getEconomy().getBalance(getPlayer(uuid));
    }
}
