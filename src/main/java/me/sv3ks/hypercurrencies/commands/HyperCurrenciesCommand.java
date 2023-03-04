package me.sv3ks.hypercurrencies.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static me.sv3ks.hypercurrencies.utils.Utils.wrap;

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

        if (args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(msgWrap("&aCommand arguments:"));
            sender.sendMessage(wrap("&a/hyper help &8- &7Shows this list."));
            sender.sendMessage(wrap("&a/hyper <reload|rl> &8- &7Reloads HyperCurrencies' data."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
