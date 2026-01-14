package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CrossbowData implements MetaManager {
    private static final CrossbowData instance = new CrossbowData();

    private CrossbowData() {

    }

    public static CrossbowData getInstance() {
        return instance;
    }

    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (!(meta instanceof CrossbowMeta crossbowMeta)) {
            return;
        }

        if (!config.getBoolean(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.crossbowLoaded, true)) {
            return;
        }

        crossbowMeta.setChargedProjectiles(List.of(new ItemStack(Material.ARROW)));
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.crossbowLoaded, getValue(item));
    }
    
    public boolean getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (!(meta instanceof CrossbowMeta crossbowMeta)) {
            return false;
        }

        return crossbowMeta.hasChargedProjectiles();
    }
}
