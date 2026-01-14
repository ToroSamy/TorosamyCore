package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayName implements MetaManager {
    private static final DisplayName instance = new DisplayName();

    private DisplayName() {

    }

    public static DisplayName getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.display;
        
        String displayString = config.getString(label, null);
        
        if (displayString == null) {
            return;
        }
        
        String component = player == null ?
                MessageUtil.format(displayString) :
                MessageUtil.format(player, displayString);
        
        meta.setDisplayName(component);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String name = getValue(item);
        
        if ("空气".equals(name)) {
            return;
        }

        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.display;

        config.set(label, name);
    }

    public String getValue(ItemStack item) {
        if (item == null) {
            return "空气";
        }
        String materialName = ConfigUtil.getMaterialChineseName(item.getType());

        if (!item.hasItemMeta()) {
            return materialName;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return materialName;
        }

        return meta.getDisplayName();
    }
}
