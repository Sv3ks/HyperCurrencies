package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getDataConfig;

public class DefaultProvider extends CurrencyProvider {

    @Override
    public String getProviderID() {
        return "hypercurrencies";
    }

    @Override
    public boolean change(ChangeType type, String name, UUID uuid, double amount) {
        Currency currency = new Currency(name);
        switch (type) {
            case ADD:

                if (
                        (get(name, uuid)+"").startsWith("-")||
                        amount+currency.getBalance(uuid)>currency.getMaxBal()
                ) {
                    return false;
                }

                getDataConfig().getConfig().set(name+"."+uuid, currency.getBalance(uuid)+amount);
                break;
            case REMOVE:
                if (
                        currency.getBalance(uuid)-amount<currency.getMinBal()||
                                (get(name, uuid)+"").startsWith("-")
                ) {
                    return false;
                }

                getDataConfig().getConfig().set(name+"."+uuid, currency.getBalance(uuid)-amount);
                break;
            case SET:
                if (
                        amount<currency.getMinBal()||
                                amount>currency.getMaxBal()
                ) {
                    return false;
                }

                getDataConfig().getConfig().set(name+"."+uuid, amount);
                break;
        }

        getDataConfig().saveConfig();
        return true;
    }

    @Override
    public double get(String name, UUID uuid) {
        if (getDataConfig().getConfig().get(name+"."+uuid)==null) {
            getDataConfig().getConfig().set(name+"."+uuid,new Currency(name).getStartingBal());
            getDataConfig().saveConfig();
        }

        return getDataConfig().getConfig().getDouble(name+"."+uuid);
    }

    @Override
    public Map<Integer,UUID> getBalanceTop(String name) {
        Currency currency = new Currency(name);
        Set<String> keys = getDataConfig().getConfig().getConfigurationSection(currency.getName()).getKeys(false);

        Map<Integer,UUID> baltop = new HashMap<>();

        for (String key : keys) {
            for (int i = 1;i!=101;i++) {
                if (currency.getBalance(UUID.fromString(key))>currency.getBalance(baltop.get(i))) {
                    for (int i2 = 100;i2!=i;i--) {
                        baltop.put(i2,baltop.get(i2-1));
                    }
                    baltop.put(i,UUID.fromString(key));
                    i=101;
                }
            }
        }

        return baltop;
    }
}
