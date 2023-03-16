package me.sv3ks.hypercurrencies.commands;

import me.sv3ks.hypercurrencies.utils.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;
import static me.sv3ks.hypercurrencies.utils.Utils.*;

public class HyperCurrenciesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0]==null) {
            sender.sendMessage(msgWrap("&cUse /hyper help for help."));
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")||args[0].equalsIgnoreCase("rl")) {

            sender.sendMessage(msgWrap("&7Reloading configs..."));

            getCurrencyConfig().reloadConfig();
            getDataConfig().reloadConfig();

            sender.sendMessage(msgWrap("&aReloaded configs!"));

            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {

            sender.sendMessage(wrap("&6&lHYPER&e&lCURRENCIES &8- &ePlugin info:"));
            sender.sendMessage(bulletWrap("Version: &e"+getPlugin().getDescription().getVersion()));
            sender.sendMessage(bulletWrap("Authors: &e"+getPlugin().getDescription().getAuthors()));
            sender.sendMessage(bulletWrap("Website: &e"+getPlugin().getDescription().getWebsite()));
            sender.sendMessage(bulletWrap("Prefix: &e"+getPlugin().getDescription().getPrefix()));

            return true;
        }

        if (args[0].equalsIgnoreCase("update")) {

            new UpdateChecker((JavaPlugin) getPlugin(), 108601).getVersion(version -> {
                if (getPlugin().getDescription().getVersion().equals(version)) {
                    sender.sendMessage(msgWrap("&aHyperCurrencies is up to date."));
                } else {
                    sender.sendMessage(msgWrap("&eThere is a new update available for HyperCurrencies."));
                }
            });

            return true;
        }


        if (args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(wrap("&6&lHYPER&e&lCURRENCIES &8- &eCommand syntax:"));
            sender.sendMessage(wrap("&8> &6/hyper help &7- &eShows this list."));
            sender.sendMessage(wrap("&8> &6/hyper <reload|rl> &7- &eReloads HyperCurrencies' data."));
            sender.sendMessage(wrap("&8> &6/hyper info &7- &eShows info about HyperCurrencies."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
