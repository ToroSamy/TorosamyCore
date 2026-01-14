package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentGlint implements MetaManager {
    private static final EnchantmentGlint instance = new EnchantmentGlint();

    private EnchantmentGlint() {

    }

    public static EnchantmentGlint getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.hideEnchantmentGlintOverride;

        meta.setEnchantmentGlintOverride(config.getBoolean(label, meta.hasEnchants()));
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.hideEnchantmentGlintOverride;

        config.set(label, getValue(item));
    }
    
    public boolean getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta.hasEnchantmentGlintOverride()) {
            return meta.getEnchantmentGlintOverride();
        }
        
        return meta.hasEnchants();
    }
}
