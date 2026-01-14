package net.torosamy.torosamyCore.commands;

import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.CommandDescription;
import org.incendo.cloud.annotations.Permission;

public class AdminCommands {
    @Command("core reload")
    @Permission("torosamycore.admin")
    @CommandDescription("重载TorosamyCore配置文件")
    public void reloadConfig(CommandSender sender) {
        ConfigUtil.reloadConfig();
        sender.sendMessage(MessageUtil.format(ConfigUtil.MAIN_CONFIG.reloadMessage));
    }

    @Command("core color <message>")
    @Permission("torosamycore.admin")
    @CommandDescription("获取格式后的颜色")
    public void color(CommandSender sender, @Argument("message")@Greedy String message) {
        String colored = MessageUtil.format(message).replaceAll("§", "#");
        sender.sendMessage(colored);
    }
}
