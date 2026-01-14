package net.torosamy.torosamyCore.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private final IConfigManage instance;
    
    private final ConfigFile configFile;
    
    public Config(IConfigManage instance, ConfigFile configFile) {
        this.instance = instance;
        this.configFile = configFile;
    }
    
    public ConfigFile getConfigFile() {
        return configFile;
    }
    
    public IConfigManage getInstance() {
        return instance;
    }
    
    public void save() {
        save(true);
    }
    public void save(boolean saveResource) {
        YamlConfiguration config = (YamlConfiguration) instance.generateConfig(configFile.getConfig());
        File file = configFile.getFile(saveResource);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void load() {
        load(true);
    }
    public void load(boolean saveResource) {
        File file = configFile.getFile(saveResource);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        instance.load(config);
    }
}
