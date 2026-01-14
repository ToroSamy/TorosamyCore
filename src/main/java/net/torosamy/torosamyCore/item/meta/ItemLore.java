package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemLore implements MetaManager {
    private static final ItemLore instance = new ItemLore();

    private ItemLore() {

    }

    public static ItemLore getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        List<String> lore = new ArrayList<>();
        
        config.getStringList(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.lore).forEach(it-> {
            String line = player == null ?
                    MessageUtil.format(it) :
                    MessageUtil.format(player, it);
            lore.add(line);
        });
        
        meta.setLore(lore);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        List<String> value = getValue(item);
        
        if (value.isEmpty()) {
            return;
        }
        
        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.lore, value);
    }
    
    public List<String> getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return List.of();
        }

        List<String> lore = item.getItemMeta().getLore();

        if (lore == null) {
            return List.of();
        }

        List<String> result = new ArrayList<>();

        lore.forEach(it-> result.add(it.replaceAll("§", "&")));

        return result;
    }
}
