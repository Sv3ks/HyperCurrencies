package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

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
                        amount+currency.getBalance(uuid)>currency.getMaxBal()
                ) {
                    return false;
                }

                getDataConfig().getConfig().set(name+"."+uuid, currency.getBalance(uuid)+amount);
                break;
            case REMOVE:
                if (
                        currency.getBalance(uuid)-amount<currency.getMinBal()
                ) {
                    return false;
                }

                getDataConfig().getConfig().set(name+"."+uuid, currency.getBalance(uuid)-amount);
                break;
            case SET:
                if (
                        currency.getBalance(uuid)-amount<currency.getMinBal()||
                                amount+currency.getBalance(uuid)>currency.getMaxBal()
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
        if (getDataConfig().getConfig().get(name+"."+uuid)!=null) new Currency(name).setBalance(uuid,new Currency(name).getStartingBal());

        return getDataConfig().getConfig().getDouble(name+"."+uuid);
    }
}
