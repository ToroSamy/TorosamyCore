package net.torosamy.torosamyCore.holder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.utils.NbtUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandItemHolder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "handitem";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Torosamy";
    }

    @Override
    public @NotNull String getVersion() {
        return TorosamyCore.plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (!(player instanceof Player onlinePlayer)) {
            return null;
        }

        EntityEquipment equipment = onlinePlayer.getEquipment();
        
        if (equipment == null) {
            return null;
        }

        ItemStack item = equipment.getItemInMainHand();
        
        if ("amount".equals(params)) {
            return String.valueOf(item.getAmount());
        }

        if ("display".equals(params)) {
            return TorosamyCoreAPI.getDisplayName(item);
        }
        
        if ("material".equals(params)) {
            return item.getType().name();
        }

        if (params.startsWith("nbt_string")) {
            String[] split = params.split("_");

            if (split.length != 3) {
                return null;
            }

            return NbtUtil.getString(item, split[2]);
        }

        if (params.startsWith("nbt_int")) {
            String[] split = params.split("_");

            if (split.length != 3) {
                return null;
            }

            return String.valueOf(NbtUtil.getInteger(item, split[2]));
        }
        
        ItemMeta meta = item.getItemMeta();
        
        if (meta == null) {
            return null;
        }

        if ("custom_model_data".equals(params)) {
            if (meta.hasCustomModelData()) {
                return String.valueOf(meta.getCustomModelData());
            }
            return null;
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        return onRequest(player, identifier);
    }

}
