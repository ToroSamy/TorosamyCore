package net.torosamy.torosamyCore.holder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NearbyHolder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "nearby";
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
        if (player == null || !player.isOnline()) {
            return null;
        }

        String[] split = params.split("_");
        if (split.length < 2) {
            return null;
        }
        
        boolean containOp = split.length > 2 && "contain-op".equals(split[3]);

        if (player instanceof Player onlinePlayer) {
            int radius = getRadius(split[1]);
            
            if (radius == -1) {
                return null;
            }

            String type = split[0];
            
            if ("near".equals(type)) {
                Entity nearbyPlayer = TorosamyCoreAPI.getNearbyEntity(onlinePlayer, radius, true, true, containOp);

                if (nearbyPlayer == null) {
                    return null;
                }

                return nearbyPlayer.getName();
            }

            if ("far".equals(type)) {
                Entity nearbyPlayer = TorosamyCoreAPI.getNearbyEntity(onlinePlayer, radius, false, true, containOp);

                if (nearbyPlayer == null) {
                    return null;
                }

                return nearbyPlayer.getName();
            }

            return null;
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return onRequest(player, params);
    }
    
    private int getRadius(String param) {
        if (param == null || param.isEmpty()) {
            return -1;
        }
        
        try {
            return Integer.parseInt(param);
        }catch (NumberFormatException exception) {
            return -1;
        }
    }

}
