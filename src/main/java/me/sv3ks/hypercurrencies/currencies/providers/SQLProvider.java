package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getCurrencyConfig;
import static me.sv3ks.hypercurrencies.HyperCurrencies.getDataConfig;
import static me.sv3ks.hypercurrencies.hooks.SQLHook.getConnection;

public class SQLProvider extends CurrencyProvider {
    @Override
    public String getProviderID() {
        return "hc-sql";
    }

    @Override
    public boolean change(ChangeType type, String name, UUID uuid, double amount) {
        final Currency currency = new Currency(name);
        final String table = getCurrencyConfig().getConfig().getString(name+".db-info.table");
        try {
            switch (type) {
                case ADD:

                    if (
                            (get(name, uuid)+"").startsWith("-") ||
                                    amount + get(name, uuid) > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+table+" SET value='"+(get(name, uuid)+amount)+"' WHERE uuid="+uuid).executeQuery();
                    break;
                case REMOVE:
                    if (
                            get(name, uuid) - amount < currency.getMinBal() ||
                                    (get(name, uuid)+"").startsWith("-")
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+table+" SET value='"+(get(name, uuid)-amount)+"' WHERE uuid="+uuid).executeQuery();
                    break;
                case SET:
                    if (
                            amount < currency.getMinBal() ||
                                    amount > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+table+" SET value='"+amount+"' WHERE uuid="+uuid).executeQuery();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        getDataConfig().saveConfig();
        return true;
    }

    @Override
    public double get(String name, UUID uuid) {
        final double balance;
        final double startingBalance = getCurrencyConfig().getConfig().getDouble(name+".starting-bal");
        final String table = getCurrencyConfig().getConfig().getString(name+".db-info.table");

        try {

            // Get balance
            ResultSet resultSet = getConnection(name).createStatement().executeQuery("SELECT * FROM "+table+" WHERE UUID='"+uuid+"'");

            // Check if balance is set
            if (resultSet.getObject("VALUE")==null) {
                // Set balance & return starting balance

                getConnection(name).prepareStatement("INSERT INTO "+table+" (uuid, value) VALUES ('"+uuid.toString()+"', '"+startingBalance+"')").executeQuery();

                return startingBalance;
            } else {
                // Get & return balance
                return Double.parseDouble(resultSet.getString("VALUE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Math.PI;
        }
    }
}
