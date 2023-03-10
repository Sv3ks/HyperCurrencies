package me.sv3ks.hypercurrencies.commands;

import me.sv3ks.hypercurrencies.utils.Currency;
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
                if (Long.parseLong(args[2])+currency.getBalance(((Player) sender).getUniqueId())>currency.getMaxBal()) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }
                addBalance(args[3], getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                sender.sendMessage(msgWrap("&aGave "+args[2]+" "+args[3]+" to "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("revoke")) {
                if (Long.parseLong(args[2])+currency.getBalance(((Player) sender).getUniqueId())<currency.getMinBal()) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }
                removeBalance(args[3], getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                sender.sendMessage(msgWrap("&aRemoved "+args[2]+" "+args[3]+" from "+sender.getName()));
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (
                        Long.parseLong(args[2])+currency.getBalance(((Player) sender).getUniqueId())<currency.getMinBal()||
                        Long.parseLong(args[2])+currency.getBalance(((Player) sender).getUniqueId())>currency.getMaxBal()) {
                    sender.sendMessage(msgWrap("&cThe player cannot have that amount money."));
                    return false;
                }
                setBalance(args[3], getPlayer(args[1]).getUniqueId(), Long.parseLong(args[2]));
                sender.sendMessage(msgWrap("&aSet "+sender.getName()+"'s "+args[3]+" to "+args[2]));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("info")||args[0].equalsIgnoreCase("get")) {
            if (args[2]==null) {
                sender.sendMessage(msgWrap("&cInvalid amount of arguments."));
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

            sender.sendMessage(msgWrap("&aCommand arguments:"));
            sender.sendMessage(wrap("&d/currency help &8- &5Shows this list."));
            sender.sendMessage(wrap("&d/currency set <player> <amount> <currency> &8- &5Sets a player's currency."));
            sender.sendMessage(wrap("&d/currency <add|give> <player> <amount> <currency> &8- &5Gives currency to a player."));
            sender.sendMessage(wrap("&d/currency <remove|revoke> <player> <amount> <currency> &8- &5Removes currency from a player."));
            sender.sendMessage(wrap("&d/currency <info|get> <player> <currency> &8- &5Gets a player's currency."));
            sender.sendMessage(wrap("&d/currency create <name> &8- &5Creates a new currency."));
            sender.sendMessage(wrap("&d/currency delete <name> &8- &5Deletes a currency."));

            return true;
        }

        sender.sendMessage(msgWrap("&cUnknown argument. Use /hyper help for help."));
        return false;
    }
}
