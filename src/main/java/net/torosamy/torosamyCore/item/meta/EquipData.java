package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.config.MainConfig;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

public class EquipData implements MetaManager {
    private static final EquipData instance = new EquipData();

    private EquipData() {

    }

    public static EquipData getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (meta == null) {
            return;
        }

        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;
        
        String equipSlot = config.getString(keys.equipSlot, null);

        if (equipSlot == null) {
            return;
        }

        EquippableComponent equippable = meta.getEquippable();


        equippable.setSlot(EquipmentSlot.valueOf(equipSlot.toUpperCase()));
        
        String equipNamespace = config.getString(keys.equipNamespace, null);
        String equipKey = config.getString(keys.equipKey, null);
        
        if (equipNamespace != null && equipKey != null) {
            equippable.setModel(new NamespacedKey(equipNamespace, equipKey));
        }
        
        equippable.setEquipOnInteract(config.getBoolean(keys.equipFastWear, false));
        meta.setEquippable(equippable);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        MainConfig.ItemAttributeKeys keys = ConfigUtil.MAIN_CONFIG.itemAttributeKeys;
        
        String namespace = getNamespace(item);
        if (!namespace.equals(NamespacedKey.MINECRAFT_NAMESPACE)) {
            config.set(keys.equipNamespace, namespace);
        }

        String key = getKey(item);
        if (!key.equals(item.getType().name())) {
            config.set(keys.equipKey, key);
        }


        EquipmentSlot slot = getSlot(item);
        if (slot != null) {
            config.set(keys.equipSlot, slot.name());
        }

        config.set(keys.equipFastWear, canFastWear(item));
    }
    
    private EquippableComponent component(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasEquippable()) {
            return null;
        }

        return meta.getEquippable();
    }
    
    public EquipmentSlot getSlot(ItemStack item) {
        EquippableComponent equippable = component(item);

        if (equippable == null) {
            return null;
        }

        return equippable.getSlot();
    }

    public String getNamespace(ItemStack item) {
        EquippableComponent equippable = component(item);

        if (equippable == null) {
            return NamespacedKey.MINECRAFT_NAMESPACE;
        }

        NamespacedKey model = equippable.getModel();
        
        if (model == null) {
            return NamespacedKey.MINECRAFT_NAMESPACE;
        }


        return model.getNamespace();
    }

    public String getKey(ItemStack item) {
        EquippableComponent equippable = component(item);

        if (equippable == null) {
            return item.getType().name();
        }

        NamespacedKey model = equippable.getModel();

        if (model == null) {
            return item.getType().name();
        }


        return model.getKey();
    }

    public boolean canFastWear(ItemStack item) {
        EquippableComponent equippable = component(item);

        if (equippable == null) {
            return false;
        }
        

        return equippable.isEquipOnInteract();
    }
}
