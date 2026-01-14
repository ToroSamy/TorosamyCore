package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.tag.DamageTypeTags;

public class FireResistantData implements MetaManager {
    private static final FireResistantData instance = new FireResistantData();

    private FireResistantData() {

    }

    public static FireResistantData getInstance() {
        return instance;
    }

    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.fireResistant;
        
        if (config.getBoolean(label, true)) {
            meta.setDamageResistant(DamageTypeTags.IS_FIRE);
        }
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.fireResistant;

        config.set(label, getValue(item));
    }

    public boolean getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasDamageResistant()) {
            return false;
        }
        
        return meta.getDamageResistant().getKey().getKey().equals(
            DamageTypeTags.IS_FIRE.getKey().getKey()        
        );
    }
}
