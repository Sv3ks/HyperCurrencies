package me.sv3ks.hypercurrencies.hooks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getCurrencyConfig;

public class SQLHook {
    public static Connection getConnection(String currency) throws SQLException {
        final String currencyPath = currency+".db-info.";
        final String url = getCurrencyConfig().getConfig().getString(currencyPath+"url");
        final String username = getCurrencyConfig().getConfig().getString(currencyPath+"username");
        final String password = getCurrencyConfig().getConfig().getString(currencyPath+"password");

        return DriverManager.getConnection(url,username,password);
    }
}
