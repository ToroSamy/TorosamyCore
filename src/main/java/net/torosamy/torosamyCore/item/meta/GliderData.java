package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GliderData implements MetaManager {
    private static final GliderData instance = new GliderData();

    private GliderData() {

    }

    public static GliderData getInstance() {
        return instance;
    }

    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }

        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.glider;

        meta.setGlider(config.getBoolean(label, false));
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.glider;

        config.set(label, getValue(item));
    }

    public boolean getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        return meta.isGlider();
    }
}
