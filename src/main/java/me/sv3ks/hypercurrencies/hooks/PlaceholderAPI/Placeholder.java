package me.sv3ks.hypercurrencies.hooks.PlaceholderAPI;

public abstract class Placeholder {
    public abstract String getName();
    public abstract int getArgsLength();
    public abstract String getPlaceholder(String[] params);
}
