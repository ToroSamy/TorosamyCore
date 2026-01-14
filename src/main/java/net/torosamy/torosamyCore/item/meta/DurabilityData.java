package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class DurabilityData implements MetaManager{
    private static final DurabilityData instance = new DurabilityData();

    private DurabilityData() {

    }

    public static DurabilityData getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (!(meta instanceof Damageable damageable)) {
            return;
        }
        int durability = config.getInt(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.durability, 0);
        if (durability != 0) {
            damageable.setDamage(durability);
        }

        int maxDurability = config.getInt(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.maxDurability, 0);

        if (maxDurability != 0) {
            damageable.setMaxDamage(maxDurability);
        }

    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        ItemMeta meta = item.getItemMeta();
        
        if (!(meta instanceof Damageable damageable)) {
            return;
        }

        if (damageable.hasDamage()) {
            config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.durability, damageable.getDamage());
        }
        
        if (damageable.hasMaxDamage()) {
            config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.maxDurability, damageable.getMaxDamage());
        }
    }
}
