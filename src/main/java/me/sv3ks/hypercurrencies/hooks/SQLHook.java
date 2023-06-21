package me.sv3ks.hypercurrencies.hooks;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getCurrencyConfig;


public class SQLHook {
    public static Connection getConnection(String currency) throws SQLException {
        String configStr = getCurrencyConfig().getConfig().saveToString();
        Yaml yaml = new Yaml();

        Map<String, String> map = (Map<String, String>) yaml.load(configStr);
        JSONObject jsonObject = new JSONObject(map);

        if (!jsonObject.containsKey(currency)) {
            return null;
        }

        JSONObject currencyJson = jsonObject.getJSONObject(currency);
        JSONObject dbInfo = currencyJson.containsKey("db-info") ? currencyJson.getJSONObject("db-info") : new JSONObject();
        final String url = dbInfo.getString("url");
        final String username = dbInfo.getString("username");
        final String password = dbInfo.getString("password");

        Connection connection = null;


        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new RuntimeException(String.format("Currency [%s] db-info value contains null,please check your config", currency));
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(String.format("SQLHOOK get connect error: %s", e.getMessage()));
            throw e;
        }

        connection.setAutoCommit(true);

        return connection;
    }
}
