package me.sv3ks.hypercurrencies.commands.playercommands;

import me.sv3ks.hypercurrencies.currencies.Currency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.sv3ks.hypercurrencies.currencies.Currency.currencyExists;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class PayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(msgWrap("&cOnly players are allowed to execute this command."));
            return false;
        }

        Player player = (Player) sender;

        if (args.length!=3) {
            sender.sendMessage(msgWrap("&cInvalid amount of arguments. Usage: /pay <player> <amount> <currency>."));
            return false;
        }

        if (getOfflinePlayer(args[0])==null) {
            sender.sendMessage(msgWrap("&cInvalid player."));
            return false;
        }

        if (!currencyExists(args[2])) {
            sender.sendMessage(msgWrap("&cInvalid currency."));
            return false;
        }

        Currency currency = new Currency(args[2]);

        if (!currency.getPayState()) {
            sender.sendMessage(msgWrap("&cPaying is disabled for this currency."));
            return false;
        }

        double amount;

        try {
            amount = Double.parseDouble(args[1]);
        } catch (Exception e) {
            sender.sendMessage(msgWrap("&cInvalid amount."));
            return false;
        }

        if (amount<currency.getPayMin()) {
            sender.sendMessage(msgWrap(String.format("&cAmount too low (min. %s).", currency.getPayMin())));
            return false;
        }

        if (!currency.removeBalance(player.getUniqueId(),amount)) {
            sender.sendMessage(msgWrap("&cYou do not have that amount of balance."));
            return false;
        }

        if (!currency.addBalance(getOfflinePlayer(args[0]).getUniqueId(),amount)) {
            sender.sendMessage(msgWrap("&cThe player can't have that amount of balance."));
            currency.addBalance(player.getUniqueId(),amount);
            return false;
        }

        sender.sendMessage(msgWrap(String.format("&aYou payed %s %s %s.", args[0], amount, currency.getName())));
        return true;
    }
}
