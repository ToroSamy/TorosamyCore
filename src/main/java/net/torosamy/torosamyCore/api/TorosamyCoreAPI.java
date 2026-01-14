package net.torosamy.torosamyCore.api;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.server.level.EntityPlayer;
import net.torosamy.torosamyCore.commands.RunCommands;
import net.torosamy.torosamyCore.item.*;
import net.torosamy.torosamyCore.item.meta.DisplayName;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class TorosamyCoreAPI {
    public static final Random RANDOM = new Random();
    
    public static final Sound DEFAULT_SOUND = Sound.ENTITY_CAT_AMBIENT;
    
    public static String saveString(ConfigurationSection config) {
        YamlConfiguration result = new YamlConfiguration();

        config.getValues(true).forEach(result::set);
        
        String saveToString = result.saveToString();
        //去掉最后一个字符后的字符串
        return saveToString.substring(0, saveToString.length() - 1);
    }
    
    public static Sound getSound(String name) {
        NamespacedKey key = NamespacedKey.minecraft(
                name.toLowerCase().replace("_", ".")
        );
        Sound sound = Registry.SOUNDS.get(key);
        if (sound == null) {
            return DEFAULT_SOUND;
        }
        
        return sound;
    }
    
    
    public static boolean runCommands(Player player, List<String> commands) {
        return new RunCommands(Map.of()).runCommands(player, commands);
    }
    
    public static boolean runCommands(Player player, List<String> commands, Map<String, List<String>> denyCommands) {
        return new RunCommands(denyCommands).runCommands(player, commands);
    }
    
    public static boolean runCommands(Player player, List<String> commands, Map<String, List<String>> denyCommands, Map<String, String> holders) {
        return new RunCommands(denyCommands).putHolder(holders).runCommands(player, commands);
    }
    
    public static int getTotalPage(int totalSize, int pageSize) {
        if (totalSize < 0 || pageSize <= 0) {
            return -1;
        }

        return (totalSize + pageSize - 1) / pageSize;
    }


    public static int getStartIndex(int totalSize, int pageSize, int pageIndex) {
        if (totalSize <= 0 || pageSize <= 0 || pageIndex < 1) {
            return 0;
        }


        int startIndex = (pageIndex - 1) * pageSize;
        
        return Math.min(startIndex, totalSize);
    }


    public static int getEndIndex(int totalSize, int pageSize, int pageIndex) {
        int startIndex = getStartIndex(totalSize, pageSize, pageIndex);

        int theoreticalEndIndex = startIndex + pageSize;

        return Math.min(theoreticalEndIndex, totalSize);
    }
    
    public static int parseInt(String param) {
        if (param == null || param.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public static Entity getNearbyEntity(Player player, Integer radius, boolean nearest, boolean needPlayer, boolean containOp) {
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);

        if (entities.isEmpty()) {
            return null;
        }

        Location playerLocation = player.getLocation();

        int index = -1;

        double bestDistanceSquared = -1.0;

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);

            if (needPlayer && !(entity instanceof Player)) {
                continue;
            }
            
            if (!containOp && entity.isOp()) {
                continue;
            }

            if (entity.getName().equals(player.getName())) {
                continue;
            }

            double currentDistanceSquared = entity.getLocation().distanceSquared(playerLocation);

            if (index == -1) {
                index = i;
                bestDistanceSquared = currentDistanceSquared;
                continue;
            }

            if (nearest && (currentDistanceSquared < bestDistanceSquared)) {
                bestDistanceSquared = currentDistanceSquared;
                index = i;
                continue;
            }

            if (currentDistanceSquared > bestDistanceSquared) {
                bestDistanceSquared = currentDistanceSquared;
                index = i;
            }
        }

        if (index == -1) {
            return null;
        }
        
        return entities.get(index);
    }
    
    public static Map<String, ConfigurationSection> getConfigs(Plugin plugin, List<String> path) {
        Map<String, ConfigurationSection> result = new HashMap<>();

        String folderName = String.join(File.separator, path);

        File directory = new File(plugin.getDataFolder(), folderName);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (File file : directory.listFiles()) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                List<String> sonPath = new ArrayList<>(path);
                sonPath.add(fileName);
                result.putAll(getConfigs(plugin, sonPath));
                continue;
            }

            if (!fileName.endsWith(".yml")) {
                continue;
            }
            result.put(
                    fileName.replace(".yml", ""),
                    YamlConfiguration.loadConfiguration(file)
            );
        }
        return result;
    }
    
    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().g.b(packet);
    }
    
    public static void playAnimation(Player player) {
        EntityPlayer nms = ((CraftPlayer) player).getHandle();

        PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(nms, (byte)35);

        TorosamyCoreAPI.sendPacket(player, packet);
    }
    
    public static boolean isPlayerStill(Location current, Location start) {
        return current.getX() == start.getX() &&
                current.getY() == start.getY() &&
                current.getZ() == start.getZ();
    }
    
    public static boolean isCustomItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        if (meta.hasDisplayName()) {
            return true;
        }

        if (meta.hasLore()) {
            return true;
        }
        // 检查是否有自定义模型数据 (CustomModelData)，常用于自定义物品贴图
        if (meta.hasCustomModelData()) {
            if (meta.getCustomModelData() != 0) {
                return true;
            }
        }
        
        return !meta.getItemFlags().isEmpty();
    }

    public static String getDisplayName(ItemStack itemStack) {
        return DisplayName.getInstance().getValue(itemStack);
    }


    public static ConfigurationSection getConfig(ItemStack itemStack) {
        return ItemGenerator.getConfig(itemStack);
    }

    public static ItemStack generateItem(ConfigurationSection config) {
        return generateItem(config, null);
    }

    public static ItemStack generateItem(ConfigurationSection config, Player player) {
        return ItemGenerator.generateItem(config, player);
    }
}
