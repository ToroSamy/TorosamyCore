package net.torosamy.torosamyCore.config;

public class MainConfig implements IConfigManage {
    public String correctCommand;
    public String lackPermission;
    public String commandSenderError;
    public String playerType;
    public String adminType;
    public String unknownType;
    public String reloadMessage;

    public ItemAttributeKeys itemAttributeKeys = new ItemAttributeKeys();
    public class ItemAttributeKeys implements IConfigManage {
        public String display;
        public String material;
        public String lore;
        public String enchantment;
        public String itemFlagList;
        public String unbreakable;
        public String color;
        public String amount;
        public String trim;
        public String customModelData;
        public String hideEnchantmentGlintOverride;
        public String equipNamespace;
        public String equipKey;
        public String equipSlot;
        public String equipFastWear;
        public String glider;
        public String consumeFood;
        public String consumeSaturation;
        public String consumeIgnoreLimit;
        public String consumeSeconds;
        public String consumeAnimation;
        public String consumeSound;
        public String consumeParticles;
        public String playAnimation;
        public String nbtInt;
        public String nbtString;
        public String fireResistant;
        public String hornInstrument;
        public String crossbowLoaded;
        public String tool;
        public String durability;
        public String maxDurability;
    }

    public String teleportTaskMove;
    public String teleportTaskBigTitle;
    public String teleportTaskSmallTitle;
    public String teleportTaskUnsafe;
    public String teleportCooldownBypassPermission;
    
    public String timestampFormat;
}
