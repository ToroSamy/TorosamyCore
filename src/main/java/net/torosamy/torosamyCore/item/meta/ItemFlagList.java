package net.torosamy.torosamyCore.item.meta;

import com.google.common.collect.ImmutableMultimap;
import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemFlagList implements MetaManager {
    private static final ItemFlagList instance = new ItemFlagList();

    private ItemFlagList() {

    }

    public static ItemFlagList getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        for (String it : config.getStringList(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.itemFlagList)) {
            ItemFlag itemFlag = ItemFlag.valueOf(it.toUpperCase());
            
            meta.addItemFlags(itemFlag);
            
            if (itemFlag == ItemFlag.HIDE_ATTRIBUTES) {
                meta.setAttributeModifiers(ImmutableMultimap.of());
            }
        }
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        List<String> itemFlags = getValue(item);
        
        if (itemFlags.isEmpty()) {
            return;
        }

        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.itemFlagList;
        
        config.set(label, itemFlags);
    }
    
    public List<String> getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return List.of();
        }

        ItemMeta meta = item.getItemMeta();

        List<String> itemFlags = new ArrayList<>();

        meta.getItemFlags().forEach(
                (flag)-> itemFlags.add(flag.name())
        );
        
        return itemFlags;
    }
}
