package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getCurrencyConfig;
import static me.sv3ks.hypercurrencies.currencies.Currency.currencyExists;
import static me.sv3ks.hypercurrencies.utils.Utils.bulletWrap;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(msgWrap("&cOnly players can execute this command."));
            return false;
        }

        Player player = (Player) sender;

        if (args.length==0) {
            Set<String> currencyNames = getCurrencyConfig().getConfig().getKeys(false);

            sender.sendMessage(msgWrap("&eYou balances:"));

            for (String currencyName : currencyNames) {
                Currency currency = new Currency(currencyName);
                sender.sendMessage(bulletWrap("&6"+currencyName+": &e"+currency.getBalance(player.getUniqueId())+"."));
            }

            return true;
        }

        if (args.length==1) {
            if (currencyExists(args[0])) {
                sender.sendMessage(msgWrap("&6Your "+args[0]+": &e"+new Currency(args[0]).getBalance(player.getUniqueId())+"."));
                return true;
            } else if (getOfflinePlayer(args[0])!=null) {
                Set<String> currencyNames = getCurrencyConfig().getConfig().getKeys(false);

                sender.sendMessage(msgWrap("&e"+args[0]+"'s balances:"));

                for (String currencyName : currencyNames) {
                    Currency currency = new Currency(currencyName);
                    sender.sendMessage(bulletWrap("&6"+currencyName+": &e"+currency.getBalance(getOfflinePlayer(args[0]).getUniqueId())+"."));
                }

                return true;
            } else {
                sender.sendMessage(msgWrap("&c"+args[0]+" is neither a currency nor player."));
                return false;
            }
        }

        if (args.length==2) {
            if (!currencyExists(args[0])) {
                sender.sendMessage(msgWrap("&cInvalid currency."));
                return false;
            }

            if (getOfflinePlayer(args[1])==null) {
                sender.sendMessage(msgWrap("&cInvalid player."));
                return false;
            }

            sender.sendMessage(msgWrap("&6"+args[1]+"'s "+args[0]+": &e"+new Currency(args[0]).getBalance(getOfflinePlayer(args[1]).getUniqueId())+"."));
            return true;
        }

        sender.sendMessage(msgWrap("&cInvalid amount of arguments. Usage: /bal [player|currency] [player]."));
        return false;
    }
}
