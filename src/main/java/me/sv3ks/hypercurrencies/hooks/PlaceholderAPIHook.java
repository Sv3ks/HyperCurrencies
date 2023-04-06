package me.sv3ks.hypercurrencies.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sv3ks.hypercurrencies.HyperCurrencies;
import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "hypercurrencies";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sv3ks";
    }

    @Override
    public @NotNull String getVersion() {
        return HyperCurrencies.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        String[] fixedParams = params.split("_");

        if (fixedParams[0].equalsIgnoreCase("balance")) {
            return String.valueOf(new Currency(fixedParams[1]).getBalance(player.getUniqueId()));
        }

        return null;
    }
}
