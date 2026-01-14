package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentData implements MetaManager {
    private static final EnchantmentData instance = new EnchantmentData();

    private EnchantmentData() {

    }

    public static EnchantmentData getInstance() {
        return instance;
    }

    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.enchantment;
        
        for (String it : config.getStringList(label)) {
            String[] split = it.split(":");
            if (split.length <= 1) {
                continue;
            }
            
            Enchantment enchantment = Enchantment.getByName(split[0]);

            if (enchantment == null) {
                continue;
            }

            int level = TorosamyCoreAPI.parseInt(split[1]);

            if (level == -1) {
                continue;
            }

            meta.addEnchant(enchantment, level, true);
        }
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        List<String> enchants = getValue(item);

        if (enchants.isEmpty()) {
            return;
        }

        String label = ConfigUtil.MAIN_CONFIG.itemAttributeKeys.enchantment;
        
        config.set(label, enchants);
    }
    
    public List<String> getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return List.of();
        }

        ItemMeta meta = item.getItemMeta();
        
        List<String> enchants = new ArrayList<>();

        meta.getEnchants().forEach(
                (enchant, level)-> enchants.add(enchant.getKey().getKey()+ ":" + level)
        );
        
        return enchants;
    }
}
