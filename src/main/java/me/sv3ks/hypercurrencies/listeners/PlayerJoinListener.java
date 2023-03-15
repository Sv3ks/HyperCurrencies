package me.sv3ks.hypercurrencies.listeners;

import me.sv3ks.hypercurrencies.utils.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static me.sv3ks.hypercurrencies.HyperCurrencies.getPlugin;
import static me.sv3ks.hypercurrencies.utils.Utils.msgWrap;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            new UpdateChecker((JavaPlugin) getPlugin(), 108601).getVersion(version -> {
                if (getPlugin().getDescription().getVersion().equals(version)) {
                    e.getPlayer().sendMessage(msgWrap("&aHyperCurrencies is up to date."));
                } else {
                    e.getPlayer().sendMessage(msgWrap("&eThere is a new update available for HyperCurrencies."));
                }
            });
        }
    }
}
