package net.torosamy.torosamyCore.manager;


import net.torosamy.torosamyCore.config.TorosamyConfig;

import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigManager {
    //内存区的config
    private TorosamyConfig config;
    //硬盘区的config
    private Plugin plugin;
    private String dataPath;
    private String fileName;
    private ConfigurationSection section;
    private YamlConfiguration yaml;

    public ConfigManager(TorosamyConfig config, Plugin plugin, String dataPath, String fileName) {
        this.config = config;
        this.plugin = plugin;
        this.dataPath = dataPath;
        this.fileName = fileName;
    }

    public ConfigManager(TorosamyConfig config, Plugin plugin, String dataPath, String fileName, ConfigurationSection section) {
        this.config = config;
        this.plugin = plugin;
        this.dataPath = dataPath;
        this.fileName = fileName;
        this.section = section;
    }


    public void load() {
        if(section == null) yaml = loadYaml(plugin, dataPath, fileName);
        else yaml = loadYaml(plugin,dataPath, fileName, section);
        loadData(config,yaml,"");
    }

    public void save() {
        if(section == null) yaml = loadYaml(plugin, dataPath, fileName);
        else yaml = loadYaml(plugin,dataPath, fileName, section);
        saveYaml(config, yaml, "");
    }

    public TorosamyConfig getConfig() {
        return config;
    }

    public YamlConfiguration getYaml() {return yaml;}

    //    public static void load(Plugin plugin, TorosamyConfig config, String dataPath, String fileName) {
//        YamlConfiguration yamlConfiguration = loadYaml(plugin, dataPath, fileName);
//        loadData(config, yamlConfiguration, "");
//    }
//
//    public static void load(Plugin plugin, TorosamyConfig config, String dataPath, String fileName, ConfigurationSection configSection) {
//        YamlConfiguration yamlConfiguration = loadYaml(plugin, dataPath, fileName, configSection);
//        loadData(config, yamlConfiguration, "");
//    }
    public static HashMap<String, ConfigurationSection> loadYamls(Plugin plugin, String directoryName, String prefixPath) {
        HashMap<String, ConfigurationSection> yamls = new HashMap<>();
        File directory = new File(plugin.getDataFolder(), directoryName);
        if (!directory.exists()) directory.mkdirs();
        for (File file : directory.listFiles()) {
            String filePath = prefixPath.isEmpty() ? file.getName() : prefixPath + File.separator + file.getName();
            if (file.isDirectory()) yamls.putAll(loadYamls(plugin, file.getName(), filePath));
            else if (file.getName().endsWith(".yml")) yamls.put(filePath, YamlConfiguration.loadConfiguration(file));
        }
        return yamls;
    }

    public static YamlConfiguration loadYaml(Plugin plugin, String dataPath, String fileName) {
        String path = plugin.getDataFolder().getPath() + File.separator + dataPath;
        File file = new File(path, fileName);
        if (!file.exists()) {
            if (dataPath.isEmpty()) plugin.saveResource(fileName, false);
            else plugin.saveResource(dataPath + File.separator + fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static YamlConfiguration loadYaml(Plugin plugin, String dataPath, String fileName, ConfigurationSection configSection) {
        String path = plugin.getDataFolder().getPath() + File.separator + dataPath;
        File file = new File(path, fileName);
        if (!file.exists()) {
            YamlConfiguration yamlConfig = new YamlConfiguration();
            configSection.getKeys(true).forEach(key -> yamlConfig.set(key, configSection.get(key)));
            try {
                yamlConfig.save(new File(path, fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void loadData(TorosamyConfig config, YamlConfiguration yamlConfiguration, String prefix) {
        if (!prefix.isEmpty()) prefix += ".";
        // 遍历类的所有字段
        for (Field declaredField : config.getClass().getDeclaredFields()) {
            try {
                String fieldName = MessageUtil.fieldToKey(declaredField.getName());
                // 检查字段是否为TorosamyConfig的子类或实例
                if (TorosamyConfig.class.isAssignableFrom(declaredField.getType())) {
                    loadData((TorosamyConfig) declaredField.get(config), yamlConfiguration, prefix + fieldName);
                    continue;
                }
                Object o = yamlConfiguration.get(prefix + fieldName);

                if (o instanceof MemorySection section) {
                    List<Object> list = new ArrayList<>();
                    for (String key : section.getKeys(false)) {
                        list.add(section.get(key));
                    }
                    declaredField.set(config, list);
                } else {declaredField.set(config, o);}

            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public static void saveYaml(TorosamyConfig config, YamlConfiguration yamlConfiguration, String prefix) {
        if (!prefix.isEmpty()) prefix += ".";
        // 遍历类的所有字段
        for (Field declaredField : config.getClass().getDeclaredFields()) {
            try {
                String fieldName = MessageUtil.keyTofield(declaredField.getName());
                // 检查字段是否为TorosamyConfig的子类或实例
                if (TorosamyConfig.class.isAssignableFrom(declaredField.getType())) {
                    saveYaml((TorosamyConfig) declaredField.get(config), yamlConfiguration, prefix + fieldName);
                    continue;
                }
                Object o = yamlConfiguration.get(prefix + fieldName);

                if (o instanceof MemorySection section) {
                    int index = 0;
                    List<List<Object>> list = (List<List<Object>>) declaredField.get(config);

                    for (String key : section.getKeys(false)) {
                        yamlConfiguration.set(prefix + fieldName + "." + key, list.get(index));
                        index++;
                    }

                } else yamlConfiguration.set(prefix + fieldName, o);

            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
