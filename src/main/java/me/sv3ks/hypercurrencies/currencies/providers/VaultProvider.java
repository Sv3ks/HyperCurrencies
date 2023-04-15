package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.Map;
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

        EconomyResponse response;

        switch (type) {
            case ADD:
                response = getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
            case REMOVE:
                response = getEconomy().withdrawPlayer(getPlayer(uuid),amount);
                break;
            case SET:
                // Player balance -> 0
                response = getEconomy().withdrawPlayer(getPlayer(uuid),get(name,uuid));

                // If withdrawal failed, abort.
                if (!response.transactionSuccess()) return false;

                // Player balance -> amount
                response = getEconomy().depositPlayer(getPlayer(uuid),amount);
                break;
            default:
                return false;
        }

        return response.transactionSuccess();
    }

    @Override
    public double get(String name, UUID uuid) {
        return getEconomy().getBalance(getPlayer(uuid));
    }

    @Override
    public Map<Integer, UUID> getBalanceTop(String name) {
        return null;
    }
}
