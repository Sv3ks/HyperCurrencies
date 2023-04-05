package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

import java.util.UUID;

import static me.sv3ks.hypercurrencies.hooks.VaultHook.getEconomy;
import static org.bukkit.Bukkit.getPlayer;

public class VaultProvider extends CurrencyProvider {

    @Override
    public String getProviderID() {
        return "vault";
    }

    @Override
    public boolean change(ChangeType type, String name, UUID uuid, double amount) {
        switch (type) {
            case ADD:
                getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
            case REMOVE:
                getEconomy().withdrawPlayer(getPlayer(uuid),amount);
                break;
            case SET:
                // Player balance -> 0
                getEconomy().withdrawPlayer(getPlayer(uuid),get(name,uuid));
                // Player balance -> amount
                getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
        }

        return true;
    }

    @Override
    public double get(String name, UUID uuid) {
        return getEconomy().getBalance(getPlayer(uuid));
    }
}
