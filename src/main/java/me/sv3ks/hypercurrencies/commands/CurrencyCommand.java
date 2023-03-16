package me.sv3ks.hypercurrencies.commands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getDataConfig;
import static me.sv3ks.hypercurrencies.HyperCurrencies.getProviders;
import static me.sv3ks.hypercurrencies.currencies.Currency.*;
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

            if (!getDataConfig().getConfig().contains(args[3])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            try {
                Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(msgWrap("&cInvalid amount."));
                return false;
            }

            Currency currency = new Currency(args[3]);

            if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")) {
                if (!currency.addBalance(getPlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aGave "+args[2]+" "+args[3]+" to "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
                if (!currency.removeBalance(getPlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aRemoved "+args[2]+" "+args[3]+" from "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (!currency.setBalance(getPlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]))) {
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
                if (!getDataConfig().getConfig().contains(args[1])) {
                    sender.sendMessage(msgWrap("&cUnknown currency."));
                    return false;
                }

                Currency currency = new Currency(args[1]);

                sender.sendMessage(msgWrap("&aInformation about the currency: &e"+currency.getName()));
                sender.sendMessage(bulletWrap("Starting balance: &e"+currency.getStartingBal()));
                sender.sendMessage(bulletWrap("Minimum balance: &e"+currency.getMinBal()));
                sender.sendMessage(bulletWrap("Maximum balance: &e"+currency.getMaxBal()));
                sender.sendMessage(bulletWrap("Provider: &e"+currency.getProvider().getProviderID().toUpperCase()));
            }

            if (getPlayer(args[1])==null) {
                sender.sendMessage(msgWrap("&cInvalid player."));
                return false;
            }

            if (!getDataConfig().getConfig().contains(args[2])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            sender.sendMessage(msgWrap("&a"+args[1]+" has "+new Currency(args[2]).getBalance(getPlayer(args[1]).getUniqueId())+" "+args[2]+"."));
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args[1]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (getDataConfig().getConfig().contains(args[1])) {
                sender.sendMessage(msgWrap("&cThat currency already exists!"));
                return false;
            }

            new Currency(args[1]);
            sender.sendMessage(msgWrap("&aYou created the currency "+args[1]));
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args[1]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (!getDataConfig().getConfig().contains(args[1])) {
                sender.sendMessage(msgWrap("&cInvalid currency."));
                return false;
            }

            new Currency(args[1]).delete();
            sender.sendMessage(msgWrap("&aYou deleted the currency "+args[1]));
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args[4]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (!getDataConfig().getConfig().contains(args[4])) {
                sender.sendMessage(msgWrap("&cInvalid currency."));
                return false;
            }

            Currency currency = new Currency(args[4]);

            switch (args[2]) {
                case "provider":
                    if (!getProviders().containsKey(args[3])) {
                        sender.sendMessage(msgWrap("&cInvalid provider."));
                        return false;
                    }

                    currency.setProvider(getProviders().get(args[3]));
                    sender.sendMessage(msgWrap("&aSet "+currency.getName()+"'s provider to "+currency.getProvider().getProviderID()));
                    return true;
                case "startingbal":
                    try {
                        Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(msgWrap("&cInvalid amount."));
                        return false;
                    }

                    currency.setStartingBal(Double.parseDouble(args[2]));
                    sender.sendMessage(msgWrap("&aSet "+currency.getName()+"'s starting balance to "+currency.getStartingBal()));
                    return true;
                case "minbal":
                    try {
                        Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(msgWrap("&cInvalid amount."));
                        return false;
                    }

                    currency.setMinBal(Double.parseDouble(args[2]));
                    sender.sendMessage(msgWrap("&aSet "+currency.getName()+"'s min balance to "+currency.getStartingBal()));
                    return true;
                case "maxbal":
                    try {
                        Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(msgWrap("&cInvalid amount."));
                        return false;
                    }

                    currency.setMaxBal(Double.parseDouble(args[2]));
                    sender.sendMessage(msgWrap("&aSet "+currency.getName()+"'s max balance to "+currency.getStartingBal()));
                    return true;
                default:
                    sender.sendMessage(msgWrap("&cInvalid option."));
                    return false;
            }
        }

        if (args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(wrap("&6&lHYPER&e&lCURRENCIES &8- &eCommand syntax:"));
            sender.sendMessage(wrap("&8> &6/currency help &7- &eShows this list."));
            sender.sendMessage(wrap("&8> &6/currency set <player> <amount> <currency> &7- &eSets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency <add|give> <player> <amount> <currency> &7- &eGives currency to a player."));
            sender.sendMessage(wrap("&8> &6/currency <remove|revoke> <player> <amount> <currency> &7- &eRemoves currency from a player."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <player> <currency> &7- &eGets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <currency> &7- &eGets data about a currency."));
            sender.sendMessage(wrap("&8> &6/currency set <option> <value> <currency> &7- &eSets an options' value."));
            sender.sendMessage(wrap("&8> &6/currency create <name> &7- &eCreates a new currency."));
            sender.sendMessage(wrap("&8> &6/currency delete <name> &7- &eDeletes a currency."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
