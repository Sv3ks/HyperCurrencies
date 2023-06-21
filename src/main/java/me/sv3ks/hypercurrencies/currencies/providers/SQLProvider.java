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

    private static final String TABLE_PREFIX = "table_HyperCurrencies_";

    @Override
    public String getProviderID() {
        return "hc-sql";
    }

    @Override
    public boolean change(ChangeType type, String name, UUID uuid, double amount) {
        final Currency currency = new Currency(name);

        if (!(currency.getProvider() instanceof SQLProvider)) {
            return false;
        }
        Connection connection = null;
        String tableName = TABLE_PREFIX + name;
        try {
            switch (type) {
                case ADD:

                    if (
                            (get(name, uuid) + "").startsWith("-") ||
                                    amount + get(name, uuid) > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    connection = getConnection(name);
                    connection.prepareStatement("UPDATE " + tableName + " SET value='" + (get(name, uuid) + amount) + "' WHERE uuid=" + uuid).executeUpdate();
                    break;
                case REMOVE:
                    if (
                            get(name, uuid) - amount < currency.getMinBal() ||
                                    (get(name, uuid) + "").startsWith("-")
                    ) {
                        return false;
                    }
                    connection = getConnection(name);
                    connection.prepareStatement("UPDATE " + tableName + " SET value='" + (get(name, uuid) - amount) + "' WHERE uuid=" + uuid).executeUpdate();
                    break;
                case SET:
                    // For autogeneration
                    get(name, uuid);
                    if (
                            amount < currency.getMinBal() ||
                                    amount > currency.getMaxBal()
                    ) {
                        return false;
                    }

                    connection = getConnection(name);
                    connection.prepareStatement("UPDATE " + tableName + " SET value='" + amount + "' WHERE uuid=" + uuid).executeUpdate();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        getDataConfig().saveConfig();
        return true;
    }

    @Override
    public double get(String name, UUID uuid) {
        final double startingBalance = getCurrencyConfig().getConfig().getDouble(name + ".starting-bal");
        Connection conn = null;
        try {
            conn = getConnection(name);

            String tableName = TABLE_PREFIX + name;

            // todo needs primary key or unique index
            conn.createStatement().execute(String.format("CREATE TABLE IF NOT EXISTS %s(uuid varchar(36), value double)", tableName));

            // Get balance
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM " + tableName + " WHERE UUID='" + uuid + "'");

            // Check if balance is set - IN DEVELOPMENT
            try {
                resultSet.getObject("value");
            } catch (SQLException e) {

                // Set balance & return starting balance
                conn.prepareStatement("INSERT INTO " + tableName + " (uuid, value) VALUES ('" + uuid.toString() + "', '" + startingBalance + "')").executeUpdate();
                return startingBalance;
            }

            // Player DOES have balance - Get & return balance
            return resultSet.getDouble("value");
        } catch (SQLException e) {
            e.printStackTrace();
            return Math.PI;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<Integer, UUID> getBalanceTop(String name) {
        Connection conn = null;
        try {
            Currency currency = new Currency(name);

            if (!(currency.getProvider() instanceof SQLProvider)) {
                return null;
            }
            conn = getConnection(name);
            Statement statement = conn.createStatement();
            // if table dont exists create it here?
            String sql = String.format("SELECT * FROM %s", TABLE_PREFIX + name);
            ResultSet result = statement.executeQuery(sql);
            // close statement too early will cause SQLException
            //statement.close();

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
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
