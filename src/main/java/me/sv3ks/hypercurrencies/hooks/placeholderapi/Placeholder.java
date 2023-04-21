package me.sv3ks.hypercurrencies.hooks.placeholderapi;

import org.bukkit.OfflinePlayer;

public abstract class Placeholder {
    public abstract String getName();
    public abstract int getArgsLength();
    public abstract String getPlaceholder(OfflinePlayer player, String[] params);
}
