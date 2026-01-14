package net.torosamy.torosamyCore.item;

import me.clip.placeholderapi.PlaceholderAPI;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.config.MainConfig;
import net.torosamy.torosamyCore.item.data.ConsumeData;
import net.torosamy.torosamyCore.item.data.DeathProtectionData;
import net.torosamy.torosamyCore.item.data.ToolData;
import net.torosamy.torosamyCore.item.meta.*;
import net.torosamy.torosamyCore.utils.NbtUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerator {
    public static final Material DEFAULT_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    
    private ItemGenerator() {}

    public static ConfigurationSection getConfig(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();

        if (itemStack == null) {
            return config;
        }

        Material material = itemStack.getType();
        if (material == Material.AIR) {
            return config;
        }

        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;

        config.set(keys.material, material.name());
        config.set(keys.amount, itemStack.getAmount());

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return config;
        }

        CustomModelData.getInstance().setConfig(itemStack, config);
        DisplayName.getInstance().setConfig(itemStack, config);
        EquipData.getInstance().setConfig(itemStack, config);
        FoodData.getInstance().setConfig(itemStack, config);
        TrimData.getInstance().setConfig(itemStack, config);
        ItemLore.getInstance().setConfig(itemStack, config);
        EnchantmentData.getInstance().setConfig(itemStack, config);
        ItemFlagList.getInstance().setConfig(itemStack, config);
        LeatherColor.getInstance().setConfig(itemStack, config);
        EnchantmentGlint.getInstance().setConfig(itemStack, config);
        UnbreakableData.getInstance().setConfig(itemStack, config);
        FireResistantData.getInstance().setConfig(itemStack, config);
        GliderData.getInstance().setConfig(itemStack, config);
        MusicInstrumentData.getInstance().setConfig(itemStack, config);
        CrossbowData.getInstance().setConfig(itemStack, config);
        DurabilityData.getInstance().setConfig(itemStack, config);

        ConsumeData.getInstance().setConfig(itemStack, config);
        DeathProtectionData.getInstance().setConfig(itemStack, config);
        ToolData.getInstance().setConfig(itemStack, config);
        return config;
    }


    public static ItemStack generateItem(ConfigurationSection config, Player player) {
        if (config == null) {
            return new ItemStack(DEFAULT_MATERIAL);
        }

        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;

        Material material = Material.getMaterial(
                config.getString(keys.material, DEFAULT_MATERIAL.name()).toUpperCase()
        );
        if (material == null) {
            material = DEFAULT_MATERIAL;
        }
        
        ItemStack itemStack = new ItemStack(material, config.getInt(keys.amount, 1));

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return itemStack;
        }

        CustomModelData.getInstance().setMeta(player, itemMeta, config);
        DisplayName.getInstance().setMeta(player, itemMeta, config);
        EquipData.getInstance().setMeta(player, itemMeta, config);
        FoodData.getInstance().setMeta(player, itemMeta, config);
        TrimData.getInstance().setMeta(player, itemMeta, config);
        ItemLore.getInstance().setMeta(player, itemMeta, config);
        EnchantmentData.getInstance().setMeta(player, itemMeta, config);
        ItemFlagList.getInstance().setMeta(player, itemMeta, config);
        LeatherColor.getInstance().setMeta(player, itemMeta, config);
        EnchantmentGlint.getInstance().setMeta(player, itemMeta, config);
        UnbreakableData.getInstance().setMeta(player, itemMeta, config);
        FireResistantData.getInstance().setMeta(player, itemMeta, config);
        GliderData.getInstance().setMeta(player, itemMeta, config);
        MusicInstrumentData.getInstance().setMeta(player, itemMeta, config);
        CrossbowData.getInstance().setMeta(player, itemMeta, config);
        DurabilityData.getInstance().setMeta(player, itemMeta, config);
        
        itemStack.setItemMeta(itemMeta);
        ConsumeData.getInstance().setItem(itemStack, config);
        DeathProtectionData.getInstance().setItem(itemStack, config);
        ToolData.getInstance().setItem(itemStack, config);
        

        
        for (String nbtString : config.getStringList(keys.nbtString)) {
            String[] split = nbtString.split(":");
            if (split.length != 2) {
                continue;
            }

            String value = PlaceholderAPI.setPlaceholders(player, split[1]);

            NbtUtil.setString(itemStack, split[0], value);
        }


        for (String nbtInt : config.getStringList(keys.nbtInt)) {
            String[] split = nbtInt.split(":");
            if (split.length != 2) {
                continue;
            }

            String value = PlaceholderAPI.setPlaceholders(player, split[1]);

            NbtUtil.setInteger(itemStack, split[0], TorosamyCoreAPI.parseInt(value));
        }

        return itemStack;
    }
}
