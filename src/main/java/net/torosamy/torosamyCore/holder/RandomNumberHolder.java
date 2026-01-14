package net.torosamy.torosamyCore.holder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomNumberHolder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "randomnumber";
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
        String[] split = params.split("_");
        
        if (split.length == 1) {
            try {
                int max = Integer.parseInt(split[0]);
                return String.valueOf(TorosamyCoreAPI.RANDOM.nextInt(max + 1));
            }catch (NumberFormatException exception) {
                return String.valueOf(-1);
            }
        }
        
        if (split.length == 2) {
            try {
                int min = Integer.parseInt(split[0]);
                int max = Integer.parseInt(split[1]);
                return String.valueOf(TorosamyCoreAPI.RANDOM.nextInt(max - min + 1) + min);
            }catch (NumberFormatException exception) {
                return String.valueOf(-1);
            }
        }
        
        return String.valueOf(-1);
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        return onRequest(player, identifier);
    }

}
