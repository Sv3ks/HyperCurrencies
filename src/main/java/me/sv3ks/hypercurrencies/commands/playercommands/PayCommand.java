package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import me.sv3ks.hypercurrencies.utils.LanguageHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sv3ks.hypercurrencies.currencies.Currency.currencyExists;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        LanguageHandler lang = new LanguageHandler();

        if (!(sender instanceof Player)) {
            sender.sendMessage(lang.getMessage("console-sender"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length==0) {
            for (String message : lang.getRawMessageSet("pay-help")) {
                sender.sendMessage(message);
            }

            return true;
        }

        if (args.length!=3) {
            sender.sendMessage(lang.getMessage("pay-invalid"));
            return false;
        }

        if (getOfflinePlayer(args[0])==null) {
            sender.sendMessage(lang.getMessage("invalid-player"));
            return false;
        }

        if (!currencyExists(args[2])) {
            sender.sendMessage(lang.getMessage("invalid-currency"));
            return false;
        }

        Currency currency = new Currency(args[2]);

        if (!currency.getPayState()) {
            sender.sendMessage(lang.getMessage("pay-disabled"));
            return false;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (Exception e) {
            sender.sendMessage(lang.getMessage("invalid-amount"));
            return false;
        }

        if (amount<currency.getPayMin()) {
            sender.sendMessage(lang.getMessage("pay-too-low")
                    .replace("{MIN}",String.valueOf(currency.getPayMin()))
            );
            return false;
        }

        if (!currency.removeBalance(player.getUniqueId(),amount)) {
            sender.sendMessage(lang.getMessage("pay-broke"));
            return false;
        }

        if (!currency.addBalance(getOfflinePlayer(args[0]).getUniqueId(),amount)) {
            sender.sendMessage(msgWrap("&cThe player can't have that amount of balance."));
            currency.addBalance(player.getUniqueId(),amount);
            return false;
        }

        sender.sendMessage(lang.getMessage("pay-transaction")
                .replace("{PLAYER}",getOfflinePlayer(args[0]).getName())
                .replace("{AMOUNT}",args[1])
                .replace("{CURRENCY}",currency.getName())
        );
        if (getOfflinePlayer(args[0]).isOnline()) getPlayer(args[0]).sendMessage(lang.getMessage("paid")
                .replace("{PLAYER}",sender.getName()
                .replace("{AMOUNT}",args[1])
                .replace("{CURRENCY}",currency.getName())
        ));
        return true;
    }
}
