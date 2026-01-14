package net.torosamy.torosamyCore.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigFile {
    private final List<String> path = new ArrayList<>();

    private final Plugin plugin;

    private final String fileName;

    public ConfigFile(Plugin plugin, String fileName, List<String> path) {
        this.plugin = plugin;
        this.fileName = fileName;

        if (path != null) {
            this.path.addAll(path);
        }
    }

    public ConfigFile(Plugin plugin, String fileName) {
        this(plugin, fileName, null);
    }
    
    public YamlConfiguration getConfig() {
        return getConfig(true);
    }
    
    public YamlConfiguration getConfig(boolean saveResource) {
        File file = getFile(saveResource);
        return YamlConfiguration.loadConfiguration(file);
    }

    public Boolean exists() {
        File file = getFile(false);
        return file.exists();
    }
    
    public File getFile() {
        return getFile(true);
    }
    
    public File getFile(boolean saveResource) {
        String path = plugin.getDataFolder() + File.separator;

        String resourceName = fileName;

        if (!this.path.isEmpty()) {
            path += String.join(File.separator, this.path);

            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            resourceName = String.join("/", this.path) + "/" + fileName;
        }

        File file = new File(path, fileName);

        if (!file.exists() && saveResource) {
            plugin.saveResource(resourceName, false);
        }

        return file;
    }
}
