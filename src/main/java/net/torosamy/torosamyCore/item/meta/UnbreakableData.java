package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UnbreakableData implements MetaManager {
    private static final UnbreakableData instance = new UnbreakableData();

    private UnbreakableData() {

    }

    public static UnbreakableData getInstance() {
        return instance;
    }

    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.unbreakable;

        meta.setUnbreakable(config.getBoolean(label, false));
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.unbreakable;

        config.set(label, getValue(item));
    }

    public boolean getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        return meta.isUnbreakable();
    }
}
