package me.sv3ks.hypercurrencies.hooks.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sv3ks.hypercurrencies.HyperCurrencies;
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

        if (fixedParams.length==0) return null;

        for (Placeholder placeholder : HyperCurrencies.getPlaceholders()) {
            if (fixedParams.length==placeholder.getArgsLength()+1&&fixedParams[0].equalsIgnoreCase(placeholder.getName())) {
                // Max 11 params (-1 because of placeholder name also counting as one).
                String[] placeholderParams = new String[11];

                for (int i = 1; i < fixedParams.length-1; i++) {
                    placeholderParams[i-1] = fixedParams[i];
                }

                return placeholder.getPlaceholder(player,placeholderParams);
            }
        }

        return null;
    }
}
