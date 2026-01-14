package net.torosamy.torosamyCore.item.meta;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface MetaManager {
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config);
    
    public void setConfig(ItemStack item, ConfigurationSection config);
}
