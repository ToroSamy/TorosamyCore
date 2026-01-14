package net.torosamy.torosamyCore.config;

import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public interface IConfigManage {
    default YamlConfiguration generateConfig() {
        return (YamlConfiguration) generateConfig(null);
    }
    
    default ConfigurationSection generateConfig(ConfigurationSection config) {
        if (config == null) {
            config = new YamlConfiguration();
        }
        
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(SkipLoad.class)) {
                continue;
            }
            // static 跳过
            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }
            // final 跳过
            if (Modifier.isFinal(declaredField.getModifiers())) {
                continue;
            }

            declaredField.setAccessible(true);

            String configKey = MessageUtil.fieldToKey(declaredField.getName());

            try {
                if (IConfigManage.class.isAssignableFrom(declaredField.getType())) {

                    IConfigManage sonConfig = (IConfigManage) declaredField.get(this);
                    
                    config.set(configKey, sonConfig.generateConfig(config.getConfigurationSection(configKey)));
                    continue;
                }


                Object o = declaredField.get(this);

                if (o instanceof MemorySection section) {
                    List<List<Object>> list = (List<List<Object>>) declaredField.get(this);

                    List<String> keys = section.getKeys(false).stream().toList();
                    for (int i = 0; i < keys.size(); i++) {
                        section.set(keys.get(i), list.get(i));
                    }
                    continue;
                }

                config.set(configKey, o);

            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        return config;
        
    }
    
    default void load(ConfigurationSection config) {
        if (config == null) {
            return;
        }
        
        for (Field declaredField : this.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(SkipLoad.class)) {
                continue;
            }
            // static 跳过
            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }
            // final 跳过
            if (Modifier.isFinal(declaredField.getModifiers())) {
                continue;
            }

            declaredField.setAccessible(true);
            
            String configKey = MessageUtil.fieldToKey(declaredField.getName());
            try {
                if (IConfigManage.class.isAssignableFrom(declaredField.getType())) {
                    IConfigManage sonConfig = (IConfigManage) declaredField.get(this);
                    ConfigurationSection sonSection = config.getConfigurationSection(configKey);
                    if (sonConfig != null && sonSection != null) {
                        sonConfig.load(sonSection);
                    }
                    continue;
                }
                
                Object o = config.get(configKey);
                
                if (o == null && !config.contains(configKey)) {
                    continue;
                }

                //List<List<String>>
                if (o instanceof MemorySection section) {
                    List<Object> list = new ArrayList<>();
                    for (String key : section.getKeys(false)) {
                        list.add(section.get(key));
                    }
                    declaredField.set(this, list);
                    continue;
                }
                
                declaredField.set(this, o);

            } catch (IllegalAccessException exception) {
                 exception.printStackTrace();
            }
        }
    }
}