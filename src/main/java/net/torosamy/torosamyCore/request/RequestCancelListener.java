package net.torosamy.torosamyCore.request;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RequestCancelListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        RequestManager.getInstance().clearByName(event.getPlayer().getName());
    }

}
