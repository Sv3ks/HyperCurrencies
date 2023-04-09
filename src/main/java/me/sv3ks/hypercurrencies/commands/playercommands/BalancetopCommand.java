package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.utils.LanguageHandler;
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

        LanguageHandler lang = new LanguageHandler();

        if (args.length==1) {
            if (!currencyExists(args[0])) {
                sender.sendMessage(lang.getMessage("invalid-currency"));
                return false;
            }

            Currency currency = new Currency(args[0]);
            Map<Integer,UUID> baltop = currency.getProvider().getBalanceTop(currency.getName());

            if (baltop==null) {
                sender.sendMessage(lang.getMessage("baltop-no-support"));
                return false;
            }

            sender.sendMessage(lang.getMessage("baltop-1")
                    .replace("{AMOUNT}",String.valueOf(10))
                    .replace("{CURRENCY}",currency.getName())
            );

            for (int i = 0; i < 10; i++) {
                sender.sendMessage(lang.getMessage("baltop-2")
                        .replace("{RANK}",String.valueOf(i+1))
                        .replace("{PLAYER}",getOfflinePlayer(baltop.get(i+1)).getName())
                        .replace("{BALANCE}",String.valueOf(currency.getBalance(baltop.get(i+1))))
                        .replace("{CURRENCY}",currency.getName())
                );
            }

            return true;
        }

        if (args.length==2) {
            if (!currencyExists(args[0])) {
                sender.sendMessage(lang.getMessage("invalid-currency"));
                return false;
            }

            int page;

            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(lang.getMessage("invalid-amount"));
                return false;
            }

            Currency currency = new Currency(args[0]);
            Map<Integer,UUID> baltop = currency.getProvider().getBalanceTop(currency.getName());

            if (baltop==null) {
                sender.sendMessage(lang.getMessage("baltop-no-support"));
                return false;
            }

            sender.sendMessage(lang.getMessage("baltop-1")
                    .replace("{AMOUNT}",String.valueOf(page*10))
                    .replace("{CURRENCY}",currency.getName())
            );

            for (int i = 0; i < 10; i++) {
                sender.sendMessage(lang.getMessage("baltop-2")
                        .replace("{RANK}",String.valueOf(i+1+((page*10)-10)))
                        .replace("{PLAYER}",getOfflinePlayer(baltop.get(i+1+((page*10)-10))).getName())
                        .replace("{BALANCE}",String.valueOf(currency.getBalance(baltop.get(i+1+((page*10)-10)))))
                        .replace("{CURRENCY}",currency.getName())
                );
            }

            return true;
        }

        sender.sendMessage(lang.getMessage("baltop-invalid"));
        return false;
    }
}
