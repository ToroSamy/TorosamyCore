package net.torosamy.torosamyCore.config;

import net.torosamy.torosamyCore.TorosamyCore;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
    private static final List<Config> configs = new ArrayList<>();

    public static final MainConfig MAIN_CONFIG = new MainConfig();
    
    private static final ConfigFile langConfigFile = new ConfigFile(TorosamyCore.plugin, "lang.yml");
    
    private static YamlConfiguration langConfig;
    
    public static void initConfig() {
        configs.clear();
        configs.add(new Config(MAIN_CONFIG, new ConfigFile(TorosamyCore.plugin, "config.yml")));
    }

    public static void reloadConfig() {
        for (Config config : configs) {
            config.load();
        }
        langConfig = langConfigFile.getConfig(true);
    }
    
    public static String getMaterialChineseName(Material material) {
        return langConfig.getString(material.name(), "空气");
    }
    
    public static void saveConfig() {
        for (Config config : configs) {
            config.save();
        }
    }
}
