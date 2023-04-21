package me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.Placeholder;
import org.bukkit.OfflinePlayer;

public class BalancePlaceholder extends Placeholder {
    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public int getArgsLength() {
        return 1;
    }

    @Override
    public String getPlaceholder(OfflinePlayer player, String[] params) {
        return String.valueOf(new Currency(params[0]).getBalance(player.getUniqueId()));
    }
}
