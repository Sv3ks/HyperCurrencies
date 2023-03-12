package me.sv3ks.hypercurrencies.commands;

import me.sv3ks.hypercurrencies.utils.Currency;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getPlugin;
import static me.sv3ks.hypercurrencies.utils.Currency.*;
import static me.sv3ks.hypercurrencies.utils.Utils.*;
import static org.bukkit.Bukkit.getPlayer;

public class CurrencyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")||args[0].equalsIgnoreCase("set")) {
            if (args[3]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
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

            Currency currency = new Currency(args[3]);

            if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")) {
                if (!currency.addBalance(getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aGave "+args[2]+" "+args[3]+" to "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
                if (!currency.removeBalance(getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aRemoved "+args[2]+" "+args[3]+" from "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (!currency.setBalance(getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aSet "+sender.getName()+"'s "+args[3]+" to "+args[2]));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("info")||args[0].equalsIgnoreCase("get")) {
            if (args[1]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (args[2]==null) {
                if (!currencyExists(args[1])) {
                    sender.sendMessage(msgWrap("&cUnknown currency."));
                    return false;
                }

                Currency currency = new Currency(args[1]);

                sender.sendMessage(msgWrap("&aInformation about the currency: &e"+currency.getName()));
                sender.sendMessage(bulletWrap("Starting balance: &e"+currency.getStartingBal()));
                sender.sendMessage(bulletWrap("Minimum balance: &e"+currency.getMinBal()));
                sender.sendMessage(bulletWrap("Maximum balance: &e"+currency.getMaxBal()));
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

        if (args[0].equalsIgnoreCase("create")) {
            if (args[1]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (currencyExists(args[1])) {
                sender.sendMessage(msgWrap("&cThat currency already exists!"));
                return false;
            }

            createCurrency(args[1]);
            sender.sendMessage(msgWrap("&aYou created the currency "+args[1]));
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args[1]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (!currencyExists(args[1])) {
                sender.sendMessage(msgWrap("&cInvalid currency."));
                return false;
            }

            deleteCurrency(args[1]);
            sender.sendMessage(msgWrap("&aYou deleted the currency "+args[1]));
        }

        if (args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(wrap("&6&lHYPER&e&lCURRENCIES &8- &eCommand syntax:"));
            sender.sendMessage(wrap("&8> &6/currency help &7- &eShows this list."));
            sender.sendMessage(wrap("&8> &6/currency set <player> <amount> <currency> &7- &eSets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency <add|give> <player> <amount> <currency> &7- &eGives currency to a player."));
            sender.sendMessage(wrap("&8> &6/currency <remove|revoke> <player> <amount> <currency> &7- &eRemoves currency from a player."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <player> <currency> &7- &eGets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <currency> &7- &eGets data about a currency."));
            sender.sendMessage(wrap("&8> &6/currency create <name> &7- &eCreates a new currency."));
            sender.sendMessage(wrap("&8> &6/currency delete <name> &7- &eDeletes a currency."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
