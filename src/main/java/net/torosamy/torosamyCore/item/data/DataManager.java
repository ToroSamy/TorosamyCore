package net.torosamy.torosamyCore.item.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public interface DataManager {
    public void setItem(ItemStack item, ConfigurationSection config);

    public void setConfig(ItemStack item, ConfigurationSection config);
}
