package net.torosamy.torosamyCore.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.network.chat.IChatBaseComponent;
import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class MessageUtil {
    private static final JoinConfiguration JOIN_CONFIGURATION = JoinConfiguration.noSeparators();
    
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.legacySection();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    private static final GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();
    
    public static IChatBaseComponent nmsComponent(String text) {
        return nmsComponent(null, text);
    }
    
    public static IChatBaseComponent nmsComponent(Player player, String text) {
        return CraftChatMessage.fromJSON(GSON_COMPONENT_SERIALIZER.serialize(
                MINI_MESSAGE.deserialize(serialize(player, text))
        ));
    }
    
    public static Component deserialize(Player player, String message) {
        if (player != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        
        return LEGACY_COMPONENT_SERIALIZER.deserialize(message.replaceAll("&", "§"));
    }
    
    public static Component component(List<Component> components) {
        return  Component.join(JOIN_CONFIGURATION, components);
    }
    
    public static Component component(String message) {
        return component(null, message);
    }
    
    public static String serialize(String message) {
        return serialize(null, message);
    }
    
    public static String serialize(Player player, String message) {
        if (player != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        
        message = message.replace("&", "§");
        
        return message
                .replaceAll("§0", "<black>")
                .replaceAll("§1", "<dark_blue>")
                .replaceAll("§2", "<dark_green>")
                .replaceAll("§3", "<dark_aqua>")
                .replaceAll("§4", "<dark_red>")
                .replaceAll("§5", "<dark_purple>")
                .replaceAll("§6", "<gold>")
                .replaceAll("§7", "<gray>")
                .replaceAll("§8", "<dark_gray>")
                .replaceAll("§9", "<blue>")
                .replaceAll("§a", "<green>")
                .replaceAll("§b", "<aqua>")
                .replaceAll("§c", "<red>")
                .replaceAll("§d", "<light_purple>")
                .replaceAll("§e", "<yellow>")
                .replaceAll("§f", "<white>")
                .replaceAll("§k", "<obfuscated>")
                .replaceAll("§l", "<b>")
                .replaceAll("§m", "<strikethrough>")
                .replaceAll("§n", "<u>")
                .replaceAll("§o", "<i>")
                .replaceAll("§r", "<reset>");
    }
    
    public static Component component(Player player, String message) {
        return MiniMessage.miniMessage().deserialize(serialize(player, message));
    }

    public static String format(String text) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(MessageUtil.component(text));
    }

    public static String format(Player player, String text) {
        return LEGACY_COMPONENT_SERIALIZER.serialize(MessageUtil.component(player, text));
    }

    public static String fieldToKey(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder kebabCase = new StringBuilder();
        char[] chars = camelCase.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                if (!kebabCase.isEmpty()) {
                    kebabCase.append('-');
                }
                kebabCase.append(Character.toLowerCase(c));
            } else {
                kebabCase.append(c);
            }
        }
        return kebabCase.toString();
    }

    public static String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);

        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConfigUtil.MAIN_CONFIG.timestampFormat);
        return dateTime.format(formatter);
    }
    
    
    public static String keyToField(String kebabCase) {
        if (kebabCase == null || kebabCase.isEmpty()) {
            return kebabCase;
        }
        StringBuilder camelCase = new StringBuilder();
        boolean nextUpperCase = false;
        char[] chars = kebabCase.toCharArray();
        for (char c : chars) {
            if (c == '-') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCase.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCase.append(c);
                }
            }
        }
        return camelCase.toString();
    }
}
