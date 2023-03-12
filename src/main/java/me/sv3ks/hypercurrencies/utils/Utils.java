package me.sv3ks.hypercurrencies.utils;

import org.bukkit.ChatColor;

public class Utils {

    private static final String prefix = "&8&l[&6&lHYPER&e&lCURRENCIES&8&l] &r";

    public static String wrap(String text) {
        return ChatColor.translateAlternateColorCodes('&',text
                .replace("%prefix%",prefix)
        );
    }

    public static String msgWrap(String text) {
        return ChatColor.translateAlternateColorCodes('&',prefix+text
        );
    }

    public static String bulletWrap(String text) {
        return ChatColor.translateAlternateColorCodes('&',"&8> &6"+text
        );
    }
}
