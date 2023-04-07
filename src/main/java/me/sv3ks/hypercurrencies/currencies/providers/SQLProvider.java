package me.sv3ks.hypercurrencies.currencies.providers;

import me.sv3ks.hypercurrencies.currencies.ChangeType;
import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.currencies.CurrencyProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            conn.createStatement().execute(String.format("CREATE TABLE IF NOT EXISTS %s(uuid varchar(255), value varchar(255))",name));

            // Get balance
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM "+name+" WHERE UUID='"+uuid+"'");

            // Check if balance is set
            if (resultSet.getObject("VALUE")==null) {
                // Set balance & return starting balance

                conn.prepareStatement("INSERT INTO "+name+" (uuid, value) VALUES ('"+uuid.toString()+"', '"+startingBalance+"')").executeUpdate();

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


    @Deprecated
    static boolean tableExistsSQL(Connection connection, String tableName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) "
                + "FROM information_schema.tables "
                + "WHERE table_name = ?"
                + "LIMIT 1;");
        preparedStatement.setString(1, tableName);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1) != 0;
    }
}
