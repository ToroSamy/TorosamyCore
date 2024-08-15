package net.torosamy.torosamyCore.manager;

import net.torosamy.torosamyCore.config.TorosamyConfig;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private YamlConfiguration yamlConfiguration;
    private TorosamyConfig torosamyConfig;
    private Map<String, String> configValues;
    public ConfigManager(TorosamyConfig torosamyConfig) {
        this.torosamyConfig = torosamyConfig;
        this.configValues = getConfigValues(torosamyConfig.getClass());
    }

    public void init(Plugin plugin, String fileName) throws IllegalAccessException {
        loadFile(plugin, fileName);
        initConfig();
    }
    public void init(Plugin plugin, String dataPath, String fileName) throws IllegalAccessException {
        loadFile(plugin, dataPath, fileName);
        initConfig();
    }

    /**
     * 加载系统中的config实体文件
     *
     * @param plugin
     * @param dataPath
     * @param fileName
     * @return
     */
    public void loadFile(Plugin plugin, String dataPath, String fileName) {
        //拼接插件的路径结构
        String path = plugin.getDataFolder().getPath() + "/" +dataPath;
        //文件对象
        File file = new File(path, fileName);
        //不存在则保存默认
        if (!file.exists()) {plugin.saveResource(dataPath + "/" + fileName, false);}
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    public void loadFile(Plugin plugin, String fileName) {
        //拼接插件的路径结构
        String path = plugin.getDataFolder().getPath();
        //文件对象
        File file = new File(path, fileName);
        //不存在则保存默认
        if (!file.exists()) {plugin.saveResource(fileName, false);}
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void initConfig() throws IllegalAccessException {
        //遍历所有成员属性
        for (Field field : this.torosamyConfig.getClass().getDeclaredFields()) {
            //允许访问私有字段
            field.setAccessible(true);
            //获取字段名
            String fieldName = MessageUtil.fieldToKey(field.getName());
            //获取数据类型
            String fieldClazz = getTypeString(field.getType());

            //如果成员属性是Config
            if("Object".equals(fieldClazz)) {initConfig((TorosamyConfig) field.get(torosamyConfig),fieldName+".");}
            else {
                for (Map.Entry<String, String> entry : configValues.entrySet()) {
                    String section = entry.getKey();
                    String type = entry.getValue();
                    if (fieldName.equals(section)) {
                        try {initConfigValue(field, this.torosamyConfig, type, section);}
                        catch (IllegalAccessException e) {throw new RuntimeException(e);}
                        configValues.remove(section);
                        break;
                    }
                }
            }
        }
    }
    public void initConfig(TorosamyConfig config, String prefix) throws IllegalAccessException {
        for (Field field : config.getClass().getDeclaredFields()) {
            //允许访问私有字段
            field.setAccessible(true);
            //获取字段名
            String fieldName = prefix + MessageUtil.fieldToKey(field.getName());
            //获取数据类型
            String fieldClazz = getTypeString(field.getType());
            //如果成员属性是Config
            if("Object".equals(fieldClazz)) {initConfig((TorosamyConfig) field.get(config),fieldName+".");}
            else {
                for (Map.Entry<String, String> entry : configValues.entrySet()) {
                    String section = entry.getKey();
                    String type = entry.getValue();
                    if (fieldName.equals(section)) {
                        try {initConfigValue(field, config, type, section);}
                        catch (IllegalAccessException e) {throw new RuntimeException(e);}
                        configValues.remove(section);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 使用 配置文件中对应的值 给 config对象的某个field赋值
     * @param field 成员属性
     * @param config 要初始化的对象
     * @param type 成员属性的数据类型格式化后的字符串
     * @param section 配置文件中的对应值
     * @throws IllegalAccessException
     */
    public void initConfigValue(Field field,TorosamyConfig config, String type, String section) throws IllegalAccessException {
        if (type.equals("String")) {
            field.set(config, this.yamlConfiguration.getString(section));
        } else if (type.equals("Integer")) {
            field.set(config, this.yamlConfiguration.getInt(section));
        } else if (type.equals("Decimal")) {
            field.set(config, this.yamlConfiguration.getDouble(section));
        } else if (type.equals("Boolean")) {
            field.set(config, this.yamlConfiguration.getBoolean(section));
        } else if (type.equals("List$String")) {
            field.set(config, this.yamlConfiguration.getStringList(section));
        } else if (type.equals("List$Boolean")) {
            field.set(config, this.yamlConfiguration.getBooleanList(section));
        } else if (type.equals("List$Decimal")) {
            field.set(config, this.yamlConfiguration.getDoubleList(section));
        } else if (type.equals("List$Integer")) {
            field.set(config, this.yamlConfiguration.getIntegerList(section));
        }
        System.out.println("设置成功");
    }
    public static Map<String, String> getConfigValues(Class<?> clazz) {
        return getConfigValues(clazz, "");
    }

    /**
     * 获取所有键值
     *
     * @param clazz  要处理的Config类
     * @param prefix aa.bb.cc.
     * @return Map<字段名String, 字段类型String>
     */
    public static Map<String, String> getConfigValues(Class<?> clazz, String prefix) {
        Map<String, String> map = new HashMap<>();
        if (!TorosamyConfig.class.isAssignableFrom(clazz)) {
            System.out.println(clazz.getName() + " 不是 TorosamyConfig 类或其子类");
            return map;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            String fieldName = prefix + MessageUtil.fieldToKey(field.getName());
            String fieldType = getTypeString(type);
            //如果是List 则获取泛型
            if ("List".equals(fieldType)) {
                Class<?> listGenericType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                map.put(fieldName, "List$" + getTypeString(listGenericType));
            }
            // 基本类型或包装类型直接加入map
            else if (!"Object".equals(fieldType)) {
                map.put(fieldName, fieldType);
            }
            // 如果成员属性是对象类型，递归处理
            else {
                map.putAll(getConfigValues(type, fieldName + "."));
            }
        }
        return map;
    }

    public static String getTypeString(Class<?> clazz) {
        if (clazz == String.class) return "String";
        if (clazz == Integer.class || clazz == int.class || clazz == Long.class || clazz == long.class)
            return "Integer";
        if (clazz == Float.class || clazz == float.class || clazz == Double.class || clazz == double.class)
            return "Decimal";
        if (clazz == Boolean.class || clazz == boolean.class) return "Boolean";
        if (clazz == Character.class || clazz == char.class) return "Character";
        if (List.class.isAssignableFrom(clazz)) return "List";
        return "Object";
    }


}