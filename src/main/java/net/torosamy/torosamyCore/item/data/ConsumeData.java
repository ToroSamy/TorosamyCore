package net.torosamy.torosamyCore.item.data;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.config.MainConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ConsumeData implements DataManager{
    private static final ConsumeData instance = new ConsumeData();

    private ConsumeData() {

    }

    public static ConsumeData getInstance() {
        return instance;
    }
    
    @Override
    public void setItem(ItemStack item, ConfigurationSection config) {
        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;

        Consumable.Builder builder = Consumable.consumable();

        double seconds = config.getDouble(keys.consumeSeconds, -1);
        String animation = config.getString(keys.consumeAnimation, null);
        String soundKey = config.getString(keys.consumeSound, null);
        
        if (seconds == -1 && animation == null && soundKey == null) {
            return;
        }

        if (seconds != -1) {
            builder.consumeSeconds((float) seconds);
        }

        if (animation != null) {
            ItemUseAnimation itemUseAnimation = ItemUseAnimation.valueOf(animation.toUpperCase());
            builder.animation(itemUseAnimation);
        }

        if (soundKey != null) {
            soundKey = soundKey.replaceAll("_", ".");
            builder.sound(NamespacedKey.minecraft(soundKey));
        }

        builder.hasConsumeParticles(config.getBoolean(keys.consumeParticles, false));
        
        item.setData(DataComponentTypes.CONSUMABLE, builder.build());
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        if (!item.hasData(DataComponentTypes.CONSUMABLE)) {
            return;
        }

        Consumable data = item.getData(DataComponentTypes.CONSUMABLE);

        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;
        
        config.set(keys.consumeSeconds, data.consumeSeconds());
        config.set(keys.consumeAnimation, data.animation().name());
        config.set(keys.consumeSound, data.sound().value());
        config.set(keys.consumeParticles, data.hasConsumeParticles());
    }
}
