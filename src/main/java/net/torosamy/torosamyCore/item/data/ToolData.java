package net.torosamy.torosamyCore.item.data;


import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.util.TriState;
import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import io.papermc.paper.datacomponent.item.Tool;

import java.util.ArrayList;
import java.util.List;

public class ToolData implements DataManager{
    private static final ToolData instance = new ToolData();
    
    private ToolData() {

    }

    public static ToolData getInstance() {
        return instance;
    }

    private static final String PICKAXE_SET = "mineable/pickaxe";
    private static final String AXE_SET = "mineable/axe";
    private static final String SHOVEL_SET = "mineable/shovel";
    private static final String HOE_SET = "mineable/hoe";
    private static final String SHEARS_SET = "mineable/shears";

    private static final Registry<BlockType> blockRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK);
    
    @Override
    public void setItem(ItemStack item, ConfigurationSection config) {
        List<String> rules = config.getStringList(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.tool);
        if (rules.isEmpty()) {
            return;
        }

        Tool.Builder builder = Tool.tool();

        for (String rule : rules) {
            String[] options = rule.split(":");
            if (options.length != 3) {
                continue;
            }

            float speed = getSpeed(options[1]);
            if (speed == -1) {
                continue;
            }
            
            RegistryKeySet<BlockType> blocks = getBlocks(options[0]);
            
            if (blocks == null) {
                continue;
            }

            builder.addRule(Tool.rule(blocks, speed, getTriState(options[2])));
        }
        
        item.setData(DataComponentTypes.TOOL, builder.build());
    }

    
    
    private RegistryKeySet<BlockType> getBlocks(String value) {
        if (PICKAXE_SET.equals(value) || 
            AXE_SET.equals(value) ||
            SHOVEL_SET.equals(value) ||
            HOE_SET.equals(value) ||
            SHEARS_SET.equals(value)) {
            return blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(value)));
        }
        
        Material material = Material.matchMaterial(value);


        if (material != null && material.isBlock()) {
            TypedKey<BlockType> typedKey = TypedKey.create(RegistryKey.BLOCK, material.getKey());

            return RegistrySet.keySet(RegistryKey.BLOCK, typedKey);
        }

        return null;
    }
    
    private String getConfig(TriState state) {
        if (state == TriState.TRUE) {
            return "true";
        }
        
        return "false";
    }
    
    private TriState getTriState(String value) {
        if (value == null) {
            return TriState.NOT_SET;
        }
        
        if ("true".equalsIgnoreCase(value)) {
            return TriState.TRUE;
        }

        if ("false".equalsIgnoreCase(value)) {
            return TriState.FALSE;
        }
        
        return TriState.NOT_SET;
    }


    private float getSpeed(String value) {
        if (value == null || value.isEmpty()) {
            return -1;
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        if (!item.hasData(DataComponentTypes.TOOL)) {
            return;
        }

        Tool data = item.getData(DataComponentTypes.TOOL);
        if (data == null) {
            return;
        }

        List<String> rulesList = new ArrayList<>();

        RegistryKeySet<BlockType> pickaxeSet = blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(PICKAXE_SET)));
        RegistryKeySet<BlockType> axeSet = blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(AXE_SET)));
        RegistryKeySet<BlockType> shovelSet = blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(SHOVEL_SET)));
        RegistryKeySet<BlockType> hoeSet = blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(HOE_SET)));
        RegistryKeySet<BlockType> shearsSet = blockRegistry.getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft(SHEARS_SET)));

        for (Tool.Rule rule : data.rules()) {
            float speed = rule.speed() != null ? rule.speed() : 1.0f;
            String tail = ":" + speed + ":" + getConfig(rule.correctForDrops());

            RegistryKeySet<BlockType> blocks = rule.blocks();
            
            if (blocks.equals(pickaxeSet)) {
                rulesList.add(PICKAXE_SET + tail);
                continue;
            }
            if (blocks.equals(axeSet)) {
                rulesList.add(AXE_SET + tail);
                continue;
            }
            if (blocks.equals(shovelSet)) {
                rulesList.add(SHOVEL_SET + tail);
                continue;
            }
            if (blocks.equals(hoeSet)) {
                rulesList.add(HOE_SET + tail);
                continue;
            }
            if (blocks.equals(shearsSet)) {
                rulesList.add(SHEARS_SET + tail);
                continue;
            }


            for (TypedKey<BlockType> block : blocks) {
                rulesList.add(block.key().asString() + tail);
            }
        }

        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.tool, rulesList);
    }
}
