package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.Map;

import static java.util.Map.entry;

public class TrimData implements MetaManager {
    private static final TrimData instance = new TrimData();

    private TrimData() {

    }

    public static TrimData getInstance() {
        return instance;
    }

    public static final Map<String, TrimMaterial> TRIM_MATERIAL = Map.ofEntries(
            entry("amethyst", TrimMaterial.AMETHYST),
            entry("copper", TrimMaterial.COPPER),
            entry("diamond", TrimMaterial.DIAMOND),
            entry("emerald", TrimMaterial.EMERALD),
            entry("gold", TrimMaterial.GOLD),
            entry("iron", TrimMaterial.IRON),
            entry("lapis", TrimMaterial.LAPIS),
            entry("netherite", TrimMaterial.NETHERITE),
            entry("quartz", TrimMaterial.QUARTZ),
            entry("redstone", TrimMaterial.REDSTONE),
            entry("resin", TrimMaterial.RESIN)
    );

    public static final Map<String, TrimPattern> TRIM_PATTERN = Map.ofEntries(
            entry("bolt", TrimPattern.BOLT),
            entry("coast", TrimPattern.COAST),
            entry("dune", TrimPattern.DUNE),
            entry("eye", TrimPattern.EYE),
            entry("flow", TrimPattern.FLOW),
            entry("host", TrimPattern.HOST),
            entry("raiser", TrimPattern.RAISER),
            entry("rib", TrimPattern.RIB),
            entry("sentry", TrimPattern.SENTRY),
            entry("wild", TrimPattern.WILD),
            entry("shaper", TrimPattern.SHAPER),
            entry("silence", TrimPattern.SILENCE),
            entry("snout", TrimPattern.SNOUT),
            entry("spire", TrimPattern.SPIRE),
            entry("tide", TrimPattern.TIDE),
            entry("vex", TrimPattern.VEX),
            entry("ward", TrimPattern.WARD),
            entry("wayfinder", TrimPattern.WAYFINDER)
    );
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }
        
        String trim = config.getString(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.trim, null);
        
        setMeta(player, meta, trim);
    }


    public void setMeta(Player player, ItemMeta meta, String trim) {
        if (meta == null) {
            return;
        }
        
        if (trim == null || !(meta instanceof ArmorMeta armorMeta)) {
            return;
        }

        String[] split = trim.split(":");
        if (split.length != 2) {
            return;
        }

        TrimPattern trimPattern = TRIM_PATTERN.get(split[0]);
        TrimMaterial trimMaterial = TRIM_MATERIAL.get(split[1]);

        if (trimMaterial == null || trimPattern == null) {
            return;
        }

        ArmorTrim armorTrim = new ArmorTrim(trimMaterial, trimPattern);
        armorMeta.setTrim(armorTrim);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        if (!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        
        if (!(meta instanceof ArmorMeta armorMeta)) {
            return;
        }
        
        ArmorTrim armorTrim = armorMeta.getTrim();
        
        if (armorTrim == null) {
            return;
        }
        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.trim, getName(armorTrim.getPattern()) + ":" + getName(armorTrim.getMaterial()));
    }
    private String getName(TrimMaterial material) {
        for (String result : TRIM_MATERIAL.keySet()) {
            TrimMaterial trimMaterial = TRIM_MATERIAL.get(result);

            if (trimMaterial == material) {
                return result;
            }
        }

        return null;
    }

    private String getName(TrimPattern pattern) {
        for (String result : TRIM_PATTERN.keySet()) {
            TrimPattern trimPattern = TRIM_PATTERN.get(result);

            if (trimPattern == pattern) {
                return result;
            }
        }

        return null;
    }
    
}
