package net.torosamy.torosamyCore.commands;

import net.torosamy.torosamyCore.utils.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

public class AdminCommands {
    @Command("tc reload")
    @Permission("torosamycore.reload")
    @CommandDescription("重载TorosamyCore配置文件")
    public void reloadConfig(CommandSender sender) {
        ConfigUtil.reloadConfig();
        sender.sendMessage(MessageUtil.text(ConfigUtil.langConfig.reloadMessage));
    }
}
