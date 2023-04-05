package me.sv3ks.hypercurrencies.commands.corecommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.sv3ks.hypercurrencies.HyperCurrencies.*;
import static me.sv3ks.hypercurrencies.currencies.Currency.currencyExists;
import static me.sv3ks.hypercurrencies.utils.Utils.*;
import static org.bukkit.Bukkit.getPlayer;

public class CurrencyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length==0||args[0].equalsIgnoreCase("help")) {

            sender.sendMessage(wrap("&6&lHYPER&e&lCURRENCIES &8- &eCommand syntax:"));
            sender.sendMessage(wrap("&8> &6/currency help &7- &eShows this list."));
            sender.sendMessage(wrap("&8> &6/currency set <player> <amount> <currency> &7- &eSets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency set <property> <value> <currency> &7- &eSets a currency's property to a specific value."));
            sender.sendMessage(wrap("&8> &6/currency <add|give> <player> <amount> <currency> &7- &eGives currency to a player."));
            sender.sendMessage(wrap("&8> &6/currency <remove|revoke> <player> <amount> <currency> &7- &eRemoves currency from a player."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <player> <currency> &7- &eGets a player's currency."));
            sender.sendMessage(wrap("&8> &6/currency <info|get> <currency> &7- &eGets data about a currency."));
            sender.sendMessage(wrap("&8> &6/currency create <name> &7- &eCreates a new currency."));
            sender.sendMessage(wrap("&8> &6/currency delete <name> &7- &eDeletes a currency."));

            return true;
        }

        if (args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("give")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
            if (args.length!=4) {
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

                sender.sendMessage(msgWrap("&aGave "+args[2]+" "+args[3]+" to "+args[1]));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
                if (!currency.removeBalance(getPlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aRemoved "+args[2]+" "+args[3]+" from "+args[1]));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("info")||args[0].equalsIgnoreCase("get")) {
            if (!(args.length>=2)) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            // Currency Information
            if (args.length==2) {
                if (!currencyExists(args[1])) {
                    sender.sendMessage(msgWrap("&cUnknown currency."));
                    return false;
                }

                Currency currency = new Currency(args[1]);

                sender.sendMessage(msgWrap("&aInformation about the currency: &e"+currency.getName()));
                sender.sendMessage(bulletWrap("Starting balance: &e"+currency.getStartingBal()));
                sender.sendMessage(bulletWrap("Minimum balance: &e"+currency.getMinBal()));
                sender.sendMessage(bulletWrap("Maximum balance: &e"+currency.getMaxBal()));
                sender.sendMessage(bulletWrap("Provider: &e"+currency.getProvider().getProviderID().toUpperCase()));

                return true;
            }

            if (getPlayer(args[1])==null) {
                sender.sendMessage(msgWrap("&cInvalid player."));
                return false;
            }

            if (!currencyExists(args[2])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            sender.sendMessage(msgWrap("&a"+args[1]+" has "+new Currency(args[2]).getBalance(getPlayer(args[1]).getUniqueId())+" "+args[2]+"."));

            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length!=2) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (currencyExists(args[1])) {
                sender.sendMessage(msgWrap("&cThat currency already exists!"));
                return false;
            }

            new Currency(args[1]);
            sender.sendMessage(msgWrap("&aYou created the currency "+args[1]));

            return true;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length!=2) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (!currencyExists(args[1])) {
                sender.sendMessage(msgWrap("&cInvalid currency."));
                return false;
            }

            new Currency(args[1]).delete();
            sender.sendMessage(msgWrap("&aYou deleted the currency "+args[1]));

            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length!=4) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
                return false;
            }

            if (getPlayer(args[1])!=null) {
                // Set balance

                if (!currencyExists(args[3])) {
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

                if (!currency.setBalance(getPlayer(args[1]).getUniqueId(), Double.parseDouble(args[2]))) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }

                sender.sendMessage(msgWrap("&aSet "+args[1]+"'s "+args[3]+" to "+args[2]));
                return true;
            }

            if (!currencyExists(args[3])) {
                sender.sendMessage(msgWrap("&cUnknown currency."));
                return false;
            }

            Currency currency = new Currency(args[3]);

            switch (args[1]) {
                case "provider":
                    if (!getProviders().containsKey(args[2])) {
                        sender.sendMessage(msgWrap("&cInvalid provider."));
                        return false;
                    }

                    currency.setProvider(getProviders().get(args[2]));
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

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
