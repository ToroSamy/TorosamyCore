package net.torosamy.torosamyCore.teleport;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.api.TorosamyCoreAPI;
import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class TeleportTask extends BukkitRunnable {
    private final Player player;
    private final Location targetLocation;
    private final Location startLocation;
    private int countdown;

    public static boolean startTask(int cooldown, Player player, Location targetLocation) {
        if (player == null || targetLocation == null) {
            return false;
        }
        new TeleportTask(cooldown, player, targetLocation)
                .runTaskTimer(TorosamyCore.plugin, 0L, 20L);
        return true;
    }

    private TeleportTask(int cooldown, Player player, Location targetLocation) {
        this.player = player;
        this.targetLocation = targetLocation;
        this.startLocation = player.getLocation();
        this.countdown = cooldown;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }
        
        if (targetLocation.getWorld() == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.MAIN_CONFIG.teleportTaskUnsafe));
            this.cancel();
            return;
        }
        
        if (!TorosamyCoreAPI.isPlayerStill(player.getLocation(), startLocation)) {
            player.sendMessage(MessageUtil.format(ConfigUtil.MAIN_CONFIG.teleportTaskMove));
            this.cancel();
            return;
        }
        
        if (player.hasPermission(ConfigUtil.MAIN_CONFIG.teleportCooldownBypassPermission) || countdown <= 0) {
            player.teleport(targetLocation);
            this.cancel();
            return;
        }
        
        String bigTitle = ConfigUtil.MAIN_CONFIG.teleportTaskBigTitle.replaceAll("%s", String.valueOf(countdown));
        String smallTitle = ConfigUtil.MAIN_CONFIG.teleportTaskSmallTitle.replaceAll("%s", String.valueOf(countdown));
        
        player.sendTitle(MessageUtil.format(bigTitle), MessageUtil.format(smallTitle));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
        countdown--;
    }
    
}