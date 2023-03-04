package me.sv3ks.hypercurrencies.utils;

import org.bukkit.ChatColor;

public class Utils {

    private static final String prefix = "&d&lHYPER&5&lCURRENCIES &8- &r";

    public static String wrap(String text) {
        return ChatColor.translateAlternateColorCodes('&',text
                .replace("%prefix%",prefix)
        );
    }

    public static String msgWrap(String text) {
        return ChatColor.translateAlternateColorCodes('&',prefix+text
        );
    }
}
