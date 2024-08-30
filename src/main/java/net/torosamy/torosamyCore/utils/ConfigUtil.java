package net.torosamy.torosamyCore.utils;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.config.LangConfig;
import net.torosamy.torosamyCore.manager.ConfigManager;

public class ConfigUtil {
    public static LangConfig langConfig = new LangConfig();
    public static ConfigManager langConfigManager = new ConfigManager(langConfig, TorosamyCore.plugin,"","lang.yml");

    public static void reloadConfig() {
        langConfigManager.load();

        System.out.println(langConfig);
    }

    public static void saveConfig() {
        langConfigManager.save();
    }
}
