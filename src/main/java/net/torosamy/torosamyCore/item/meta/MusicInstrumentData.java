package net.torosamy.torosamyCore.item.meta;

import net.torosamy.torosamyCore.config.ConfigUtil;
import org.bukkit.MusicInstrument;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

import java.util.Map;

public class MusicInstrumentData implements MetaManager {
    private static final Map<String, MusicInstrument> MUSIC_INSTRUMENT_MAP = Map.of(
            "admire_goat_horn",MusicInstrument.ADMIRE_GOAT_HORN,
            "call_goat_horn",MusicInstrument.CALL_GOAT_HORN,
            "dream_goat_horn",MusicInstrument.DREAM_GOAT_HORN,
            "feel_goat_horn",MusicInstrument.FEEL_GOAT_HORN,
            "ponder_goat_horn",MusicInstrument.PONDER_GOAT_HORN,
            "seek_goat_horn",MusicInstrument.SEEK_GOAT_HORN,
            "sing_goat_horn",MusicInstrument.SING_GOAT_HORN,
            "yearn_goat_horn",MusicInstrument.YEARN_GOAT_HORN
    );
    
    private static final MusicInstrumentData instance = new MusicInstrumentData();

    private MusicInstrumentData() {

    }

    public static MusicInstrumentData getInstance() {
        return instance;
    }
    
    @Override
    public void setMeta(Player player, ItemMeta meta, ConfigurationSection config) {
        if (!(meta instanceof MusicInstrumentMeta musicInstrumentMeta)) {
            return;
        }

        String hornInstrumentName = config.getString(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.hornInstrument, null);

        if (hornInstrumentName == null) {
            return;
        }

        MusicInstrument instrument = MUSIC_INSTRUMENT_MAP.get(hornInstrumentName.toLowerCase());

        if (instrument == null) {
            return;
        }
        
        musicInstrumentMeta.setInstrument(instrument);
    }

    @Override
    public void setConfig(ItemStack item, ConfigurationSection config) {
        String value = getValue(item);
        
        if (value == null) {
            return;
        }
        
        config.set(ConfigUtil.MAIN_CONFIG.itemAttributeKeys.hornInstrument, value);
    }
    
    public String getValue(ItemStack item) {
        if (!item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (!(meta instanceof MusicInstrumentMeta musicInstrumentMeta)) {
            return null;
        }

        MusicInstrument instrument = musicInstrumentMeta.getInstrument();
        
        if (instrument == null) {
           return null; 
        }
        
        return getName(instrument);
    }

    private String getName(MusicInstrument instrument) {
        for (String result : MUSIC_INSTRUMENT_MAP.keySet()) {
            if (MUSIC_INSTRUMENT_MAP.get(result) == instrument) {
                return result;
            }
        }

        return null;
    }
}
