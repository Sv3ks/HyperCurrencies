package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getDataConfig;
import static me.sv3ks.hypercurrencies.currencies.Currency.currencyExists;
import static me.sv3ks.hypercurrencies.utils.Utils.bulletWrap;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;

public class BalancetopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length==1) {
            if (!currencyExists(args[0])) {
                sender.sendMessage(msgWrap("&cThe specified currency does not exist."));
                return false;
            }

            Currency currency = new Currency(args[0]);

            if (!currency.getProvider().getProviderID().equalsIgnoreCase("HyperCurrencies")) {
                sender.sendMessage(msgWrap("&cThe specified currency's provider does not support balance top."));
                return false;
            }

            Set<String> keys = getDataConfig().getConfig().getConfigurationSection(currency.getName()).getKeys(false);

            Map<Integer,UUID> baltop = new HashMap<>();

            for (String key : keys) {
                for (int i = 1;i!=11;i++) {
                    if (currency.getBalance(UUID.fromString(key))>currency.getBalance(baltop.get(1))) {
                        baltop.put(10,baltop.get(9));
                        baltop.put(9,baltop.get(8));
                        baltop.put(8,baltop.get(7));
                        baltop.put(7,baltop.get(6));
                        baltop.put(6,baltop.get(5));
                        baltop.put(5,baltop.get(4));
                        baltop.put(4,baltop.get(3));
                        baltop.put(3,baltop.get(2));
                        baltop.put(2,baltop.get(1));
                        baltop.put(1,UUID.fromString(key));
                        i=11;
                    }
                }
            }

            sender.sendMessage(msgWrap("&eTop &610 &eplayers with the most &6"+currency.getName()+"&e."));
            sender.sendMessage(bulletWrap("&6&l#1: &e"+getOfflinePlayer(baltop.get(1)).getName()+" &7- &6"+currency.getBalance(baltop.get(1))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7&l#2: &e"+getOfflinePlayer(baltop.get(2)).getName()+" &7- &6"+currency.getBalance(baltop.get(2))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&c&l#3: &e"+getOfflinePlayer(baltop.get(3)).getName()+" &7- &6"+currency.getBalance(baltop.get(3))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#4: &e"+getOfflinePlayer(baltop.get(4)).getName()+" &7- &6"+currency.getBalance(baltop.get(4))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#5: &e"+getOfflinePlayer(baltop.get(5)).getName()+" &7- &6"+currency.getBalance(baltop.get(5))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#6: &e"+getOfflinePlayer(baltop.get(6)).getName()+" &7- &6"+currency.getBalance(baltop.get(6))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#7: &e"+getOfflinePlayer(baltop.get(7)).getName()+" &7- &6"+currency.getBalance(baltop.get(7))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#8: &e"+getOfflinePlayer(baltop.get(8)).getName()+" &7- &6"+currency.getBalance(baltop.get(8))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#9: &e"+getOfflinePlayer(baltop.get(9)).getName()+" &7- &6"+currency.getBalance(baltop.get(9))+" "+currency.getName()));
            sender.sendMessage(bulletWrap("&7#10: &e"+getOfflinePlayer(baltop.get(10)).getName()+" &7- &6"+currency.getBalance(baltop.get(10))+" "+currency.getName()));


            return true;
        }

        sender.sendMessage(msgWrap("&cInvalid amount of arguments. Usage: /baltop <currency>."));
        return false;
    }
}
