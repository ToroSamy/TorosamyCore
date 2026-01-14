package net.torosamy.torosamyCore.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class InventoryBlockerHolder implements InventoryHolder {
    public static final InventoryBlockerHolder INVENTORY_BLOCKER_HOLDER = new InventoryBlockerHolder();
    
    @Override
    public @NotNull Inventory getInventory() {
        throw new UnsupportedOperationException("This InventoryHolder is only used as a marker.");
    }
    
    public static boolean isBlockerInventory(Inventory inventory) {
        return inventory.getHolder() instanceof InventoryBlockerHolder;
    }
}
