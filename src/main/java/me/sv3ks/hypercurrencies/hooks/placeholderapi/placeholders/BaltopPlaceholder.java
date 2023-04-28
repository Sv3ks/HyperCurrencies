package me.sv3ks.hypercurrencies.hooks.placeholderapi.placeholders;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.hooks.placeholderapi.Placeholder;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class BaltopPlaceholder extends Placeholder {
    @Override
    public String getName() {
        return "baltop";
    }

    @Override
    public int getArgsLength() {
        return 2;
    }

    @Override
    public String getPlaceholder(OfflinePlayer player, String[] params) {
        try {Integer.parseInt(params[0]);} catch (NumberFormatException e) {

            for (Map.Entry<Integer, UUID> entry : new Currency(params[1]).getProvider().getBalanceTop(params[1]).entrySet()) {

                // %hypercurrencies_baltop_{PLAYER}_{CURRENCY}% - Returns placement
                if (entry.getValue()==getOfflinePlayer(params[0]).getUniqueId()) {
                    return String.valueOf(entry.getKey());
                }

                // %hypercurrencies_baltop_{UUID}_{CURRENCY}% - Returns placement
                if (entry.getValue()==UUID.fromString(params[0])) {
                    return String.valueOf(entry.getKey());
                }
            }
        }

        // %hypercurrencies_baltop_{PLACEMENT}_{CURRENCY}% - Returns player
        return String.valueOf(
                new Currency(params[1]).getBalance(
                        new Currency(params[1]).getProvider().getBalanceTop(params[1])
                                .get(Integer.parseInt(params[0]))
                )
        );
    }
}
