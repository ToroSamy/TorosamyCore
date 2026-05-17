package net.torosamy.torosamyCore.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryBlocker implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();

        if (MenuBlockerHolder.isBlockerInventory(inventory)) { 
            event.setCancelled(true);
        }
    }
    
}