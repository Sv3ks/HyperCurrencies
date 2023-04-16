package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;
import static me.sv3ks.hypercurrencies.hooks.SQLHook.getConnection;

public class SQLProvider extends CurrencyProvider {
    @Override
    public String getProviderID() {
        return "hc-sql";
    }

    @Override
    public boolean change(ChangeType type, String name, UUID uuid, double amount) {
        final Currency currency = new Currency(name);
        try {
            switch (type) {
                case ADD:

                    if (
                            (get(name, uuid)+"").startsWith("-") ||
                                    amount + get(name, uuid) > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+name+" SET value='"+(get(name, uuid)+amount)+"' WHERE uuid="+uuid).executeUpdate();
                    break;
                case REMOVE:
                    if (
                            get(name, uuid) - amount < currency.getMinBal() ||
                                    (get(name, uuid)+"").startsWith("-")
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+name+" SET value='"+(get(name, uuid)-amount)+"' WHERE uuid="+uuid).executeUpdate();
                    break;
                case SET:
                    // For autogeneration
                    get(name,uuid);
                    if (
                            amount < currency.getMinBal() ||
                                    amount > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    getConnection(name).prepareStatement("UPDATE "+name+" SET value='"+amount+"' WHERE uuid="+uuid).executeUpdate();
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
        final double startingBalance = getCurrencyConfig().getConfig().getDouble(name+".starting-bal");
        final Connection conn;
        try {
            conn = getConnection(name);

            conn.createStatement().execute(String.format("CREATE TABLE IF NOT EXISTS %s(uuid varchar(36), value double)",name));

            // Get balance
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM "+name+" WHERE UUID='"+uuid+"'");

            // Check if balance is set
            if (resultSet.getObject("value")==null) {
                // Set balance & return starting balance

                conn.prepareStatement("INSERT INTO "+name+" (uuid, value) VALUES ('"+uuid.toString()+"', '"+startingBalance+"')").executeUpdate();

                return startingBalance;
            } else {
                // Get & return balance
                return resultSet.getDouble("value");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Math.PI;
        }
    }

    @Override
    public Map<Integer, UUID> getBalanceTop(String name) {
        try {
            Currency currency = new Currency(name);
            Statement statement = getConnection(name).createStatement();
            String sql = String.format("SELECT * FROM %s",name);
            ResultSet result = statement.executeQuery(sql);
            statement.close();

            Map<Integer, UUID> baltop = new HashMap<>();

            while (result.next()) {
                for (int i = 1; i != 101; i++) {
                    if (result.getDouble("value") > currency.getBalance(baltop.get(i))) {
                        for (int i2 = 100; i2 != i; i--) {
                            baltop.put(i2, baltop.get(i2 - 1));
                        }
                        baltop.put(i, UUID.fromString(result.getString("uuid")));
                        i = 101;
                    }
                }
            }

            return baltop;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
