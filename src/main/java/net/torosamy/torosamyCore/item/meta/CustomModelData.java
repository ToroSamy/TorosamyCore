package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModelData implements MetaManager {
    private static final CustomModelData instance = new CustomModelData();
    
    private CustomModelData() {
        
    }
    
    public static CustomModelData getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.customModelData;

        int customModelData = config.getInt(label, -1);
        
        if (customModelData == -1) {
            return;
        }
        
        meta.setCustomModelData(customModelData);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        
        if (meta == null || !meta.hasCustomModelData()) {
            return;
        }

        int customModelData = meta.getCustomModelData();

        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.customModelData;
        
        config.set(label, customModelData);
    }

    public int getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return -1;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null || !meta.hasCustomModelData()) {
            return -1;
        }

        int customModelData = meta.getCustomModelData();
    
        return customModelData;
    }
}
