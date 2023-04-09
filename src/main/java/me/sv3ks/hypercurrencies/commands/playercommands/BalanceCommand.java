package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.utils.LanguageHandler;
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

        LanguageHandler lang = new LanguageHandler();

        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage("console-sender"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length==0) {
            Set<String> currencyNames = getCurrencyConfig().getConfig().getKeys(false);

            sender.sendMessage(lang.getMessage("bal-list"));

            for (String currencyName : currencyNames) {
                Currency currency = new Currency(currencyName);
                sender.sendMessage(lang.getMessage("bal-list-bal")
                        .replace("{CURRENCY}",currencyName)
                        .replace("{BALANCE}",String.valueOf(currency.getBalance(player.getUniqueId())))
                );
            }

            return true;
        }

        if (args.length==1) {
            if (currencyExists(args[0])) {
                sender.sendMessage(lang.getMessage("bal-specific")
                        .replace("{CURRENCY}",args[0])
                        .replace("{BALANCE}",String.valueOf(new Currency(args[0]).getBalance(player.getUniqueId())))
                );
                return true;
            } else if (getOfflinePlayer(args[0])!=null) {
                Set<String> currencyNames = getCurrencyConfig().getConfig().getKeys(false);

                sender.sendMessage(lang.getMessage("bal-other-list")
                        .replace("{PLAYER}",getOfflinePlayer(args[0]).getName())
                );

                for (String currencyName : currencyNames) {
                    Currency currency = new Currency(currencyName);
                    sender.sendMessage(lang.getMessage("bal-list-bal")
                            .replace("{CURRENCY}",currencyName)
                            .replace("{BALANCE}",String.valueOf(currency.getBalance(getOfflinePlayer(args[0]).getUniqueId())))
                    );
                }

                return true;
            } else {
                sender.sendMessage(lang.getMessage("not-currency-nor-player")
                        .replace("{TEXT}",args[0])
                );
                return false;
            }
        }

        if (args.length==2) {
            if (!currencyExists(args[0])) {
                sender.sendMessage(lang.getMessage("invalid-currency"));
                return false;
            }

            if (getOfflinePlayer(args[1])==null) {
                sender.sendMessage(lang.getMessage("invalid-player"));
                return false;
            }

            sender.sendMessage(lang.getMessage("bal-other-specific")
                    .replace("{PLAYER}",args[1])
                    .replace("{CURRENCY}",args[0])
                    .replace("{BALANCE}",String.valueOf(new Currency(args[0]).getBalance(getOfflinePlayer(args[1]).getUniqueId())))
            );
            return true;
        }

        sender.sendMessage(lang.getMessage("bal-invalid"));
        return false;
    }
}
