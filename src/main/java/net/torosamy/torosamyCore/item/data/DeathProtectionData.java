package net.torosamy.torosamyCore.item.data;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DeathProtection;
import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class DeathProtectionData implements DataManager{
    private static final DeathProtectionData instance = new DeathProtectionData();

    private DeathProtectionData() {

    }

    public static DeathProtectionData getInstance() {
        return instance;
    }
    
    @Override
    public void setItem(ItemStack item, ConfigurationSection config) {
        if (!config.getBoolean(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.playAnimation, item.getType() == Material.TOTEM_OF_UNDYING)) {
            return;
        }

        item.setData(DataComponentTypes.DEATH_PROTECTION, DeathProtection.deathProtection());
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        config.set(
                ConfigUtil.MAIN_CONFIG.itemAttributeKeys.playAnimation, item.hasData(DataComponentTypes.DEATH_PROTECTION)
        );
    }
}
