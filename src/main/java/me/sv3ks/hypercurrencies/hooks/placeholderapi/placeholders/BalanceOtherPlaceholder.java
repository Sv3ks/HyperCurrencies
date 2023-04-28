package me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.Placeholder;
import org.bukkit.OfflinePlayer;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class BalanceOtherPlaceholder extends Placeholder {
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public int getArgsLength() {
        // This is what separates BalancePlaceholder and BalanceOtherPlaceholder
        return 2;
    }

    @Override
    public String getPlaceholder(OfflinePlayer player, String[] params) {
        return String.valueOf(new Currency(params[0]).getBalance(getOfflinePlayer(params[1]).getUniqueId()));
    }
}
