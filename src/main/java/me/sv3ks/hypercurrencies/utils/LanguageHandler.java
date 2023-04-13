package me.sv3ks.hypercurrencies.utils;

import java.io.File;
import java.util.List;

import me.sv3ks.hypercurrencies.HyperCurrencies;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class LanguageHandler {

    private final Plugin plugin;
    private final FileConfiguration config;
    private final String language;

    public LanguageHandler()
    {
        plugin = HyperCurrencies.getPlugin();
        language = HyperCurrencies.getPlugin().getConfig().getString("language");
        config = YamlConfiguration.loadConfiguration(
                new File(
                        String.format(
                                this.plugin.getDataFolder()+"/lang/%s.yml",
                                language
                        )
                ));
    }

    public String getMessage(String name)
    {
        String message = config.getString(name);
        if (message == null)
        {
            this.plugin.getLogger().warning(String.format("Missing message in %s: %s",language,name));
            message = "&cMissing message.";
        }


        message = ChatColor.translateAlternateColorCodes('&', message);
        return getPrefix()+message;
    }

    public List<String> getRawMessageSet(String name) {
        List<String> messageSet = config.getStringList(name);
        if (messageSet.isEmpty())
        {
            this.plugin.getLogger().warning(String.format("Missing messageSet in %s: %s",language,name));
            messageSet.add("&cMissing messageSet.");
        }

        // Wrap

        for (String message : messageSet) {
            messageSet.remove(message);
            messageSet.add(ChatColor.translateAlternateColorCodes('&',message));
        }

        return messageSet;
    }

    public List<String> getPrefixedMessageSet(String name) {
        List<String> messageSet = getRawMessageSet(name);
        for (String message : messageSet) {
            messageSet.remove(message);
            messageSet.add(getPrefix()+message);
        }
        return messageSet;
    }

    public List<String> getBulletMessageSet(String name) {
        List<String> messageSet = getRawMessageSet(name);
        for (String message : messageSet) {
            messageSet.remove(message);
            messageSet.add(Utils.bulletWrap(message));
        }
        return messageSet;
    }

    public String getPrefix()
    {
        String prefix = config.getString("prefix");
        if (prefix == null)
        {
            this.plugin.getLogger().warning(String.format("Missing prefix message in %s",language));
            prefix = "&cMissing prefix.";
        }


        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        return prefix;
    }
}
