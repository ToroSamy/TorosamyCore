package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherColor implements MetaManager {
    private static final LeatherColor instance = new LeatherColor();

    private LeatherColor() {

    }

    public static LeatherColor getInstance() {
        return instance;
    }

    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        String color = config.getString(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.color);
        
        if(color == null || !(meta instanceof LeatherArmorMeta leatherArmorMeta)) {
            return;
        }

        leatherArmorMeta.setColor(Color.fromRGB(Integer.parseInt(color, 16)));
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String value = getValue(item);
        
        if (value == null) {
            return;
        }
        
        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.color, value);
    }
    
    public String getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if(!(meta instanceof LeatherArmorMeta leatherArmorMeta)) {
            return null;
        }

        Color color = leatherArmorMeta.getColor();

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int rgb = (r << 16) | (g << 8) | b;

        return String.format("%06X", rgb);
    }
}
