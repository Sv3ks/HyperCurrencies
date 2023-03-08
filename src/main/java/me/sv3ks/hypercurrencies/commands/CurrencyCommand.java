package me.sv3ks.hypercurrencies.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getPlugin;
import static me.sv3ks.hypercurrencies.utils.Currency.*;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static me.sv3ks.hypercurrencies.utils.Utils.wrap;
import static org.bukkit.Bukkit.getPlayer;

public class CurrencyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
            if (args[3]==null) {
                sender.sendMessage(msgWrap("&cInvalid amounts of arguments."));
                return false;
            }

            if (getPlayer(args[1])==null) {
                sender.sendMessage(msgWrap("&cInvalid player."));
                return false;
            }

            if (!currencyExists(args[3])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            try {
                Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(msgWrap("&cInvalid amount."));
                return false;
            }

            if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")) {
                addBalance(args[3], getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                sender.sendMessage(msgWrap("&aGave "+args[2]+" "+args[3]+" to "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
                addBalance(args[3], getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                sender.sendMessage(msgWrap("&aRemoved "+args[2]+" "+args[3]+" from "+sender.getName()));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("info")||args[0].equalsIgnoreCase("get")) {
            if (args[2]==null) {
                sender.sendMessage(msgWrap("&cInvalid amounts of arguments."));
                return false;
            }

            if (getPlayer(args[1])==null) {
                sender.sendMessage(msgWrap("&cInvalid player."));
                return false;
            }

            if (!currencyExists(args[2])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            sender.sendMessage(msgWrap("&a"+args[1]+" has "+getBalance(args[2],getPlayer(args[1]).getUniqueId())+" "+args[2]+"."));
        }

        if (args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(msgWrap("&aCommand arguments:"));
            sender.sendMessage(wrap("&d/currency help &8- &5Shows this list."));
            sender.sendMessage(wrap("&d/currency <add|give> <player> <amount> <currency> &8- &5Gives currency to a player."));
            sender.sendMessage(wrap("&d/currency <remove|revoke> <player> <amount> <currency> &8- &5Removes currency from a player."));
            sender.sendMessage(wrap("&d/currency <info|get> <player> <currency> &8- &5Gets a player's currency."));
            sender.sendMessage(wrap("&d/currency <create> <name> &8- &5Creates a new currency."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
