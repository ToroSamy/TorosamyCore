package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.config.MainConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

import java.util.List;

public class FoodData implements MetaManager {
    private static final FoodData instance = new FoodData();

    private FoodData() {

    }

    public static FoodData getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }

        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;
        
        FoodComponent foodComponent = meta.getFood();
        foodComponent.setCanAlwaysEat(config.getBoolean(keys.consumeIgnoreLimit, false));

        int nutrition = config.getInt(keys.consumeFood, 0);
        int saturation = config.getInt(keys.consumeSaturation, 0);

        if (nutrition >= 0) {
            foodComponent.setNutrition(nutrition);
        }

        if (saturation >= 0) {
            foodComponent.setSaturation(saturation);
        }

        meta.setFood(foodComponent);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasFood()) {
            return;
        }
        
        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;

        int nutrition = getNutrition(item);
        if (nutrition != 0) {
            config.set(keys.consumeFood, nutrition);
        }

        float saturation = getSaturation(item);
        if (saturation != 0) {
            config.set(keys.consumeSaturation, saturation);
        }
        
        config.set(keys.consumeIgnoreLimit, canAlwaysEat(item));
    }
    
    public float getSaturation(ItemStack item) {
        if (!item.hasItemMeta()) {
            return 0;
        }

        ItemMeta meta = item.getItemMeta();

        FoodComponent food = meta.getFood();

        return food.getSaturation();
    }

    public int getNutrition(ItemStack item) {
        if (!item.hasItemMeta()) {
            return 0;
        }

        ItemMeta meta = item.getItemMeta();

        FoodComponent food = meta.getFood();

        return food.getNutrition();
    }
    
    private static List<String> canAlwaysEatKeywords = List.of(
            "POTION", "MILK", "HONEY_BOTTLE"
    );

    public boolean canAlwaysEat(ItemStack item) {
        if (!item.hasItemMeta()) {
            for (String keyword : canAlwaysEatKeywords) {
                if (item.getType().name().equals(keyword)) {
                    return true;
                }
            }
            
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        FoodComponent food = meta.getFood();

        return food.canAlwaysEat();
    }
}
