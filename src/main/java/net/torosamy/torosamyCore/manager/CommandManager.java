package net.torosamy.torosamyCore.manager;


import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

public class CommandManager {
    public LegacyPaperCommandManager<CommandSender> manager;
    public AnnotationParser<CommandSender> annotationParser;

    public CommandManager() {}

    public void registerExceptionHandlers() {
        this.manager.exceptionController().registerHandler(InvalidSyntaxException.class, context -> {
//            String[] split = context.exception().correctSyntax().split(" ");
//            for (int i = 1; i < split.length; i++) {
//                if(split[i].contains("|")) {
//                    split[i] = split[i].replaceAll("\\|", " | ");
//                    split[i] = "(" + split[i] + ")";
//                }
//            }
//            StringBuilder builder = new StringBuilder();
//            builder.append(split[0]);
//            for (int i = 1; i < split.length; i++) builder.append(" ").append(split[i]);
//            String correctCommand = builder.toString();
            String correctCommand = context.exception().correctSyntax().replaceAll("\\|", "*");
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c指令语法错误 正确语法: &e/" + correctCommand));
        });

        this.manager.exceptionController().registerHandler(NoPermissionException.class, context -> {
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c缺少权限 (&e" + context.exception().missingPermission().permissionString() + "&c) 来使用指令"));
        });

        this.manager.exceptionController().registerHandler(InvalidCommandSenderException.class, context -> {
            String[] split = context.exception().requiredSenderTypes().toString().split("\\.");
            String typeString = split[split.length - 1];
            if(typeString.equals("Player]")) typeString = "玩家";
            else if(typeString.equals("ConsoleCommandSender]")) typeString="管理员";
            else typeString = "未知类型";
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c您必须是一名&e " + typeString + " &c才能这么做"));
        });
    }
}
