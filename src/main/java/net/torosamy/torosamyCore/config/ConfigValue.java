package net.torosamy.torosamyCore.config;

public class ConfigValue<T> {
    private T defaultValue;
    private T value;
    private final Class<T> type;
    private final String label;
    private ConfigValue(T value, Class<T> type, String label) {
        this.value = value;
        this.type = type;
        this.label = label;
    }
    public T getDefaultValue() {return this.defaultValue;}
    public T getValue() {return this.value;}
    public Class<T> getType() {return this.type;}
    public String getLabel() {return this.label;}
    public void setValue(T value) {this.value = value;}

    public static <T> ConfigValue<T> of(T defaultValue, Class<T> type, String label) {
        return new ConfigValue<T>(defaultValue, type, label);
    }
}
