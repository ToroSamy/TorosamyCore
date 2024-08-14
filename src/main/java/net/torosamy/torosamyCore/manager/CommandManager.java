package net.torosamy.torosamyCore.manager;


import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

public class CommandManager {
    public LegacyPaperCommandManager<CommandSender> manager;
    public AnnotationParser<CommandSender> annotationParser;

    public CommandManager() {}

    public void registerExceptionHandlers() {
        this.manager.exceptionController().registerHandler(InvalidSyntaxException.class, context -> {
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c指令语法错误! 正确语法: &e" + context.exception().correctSyntax()));
        });

        this.manager.exceptionController().registerHandler(NoPermissionException.class, context -> {
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c缺少权限 (&e" + context.exception().missingPermission().permissionString() + "&c) 来使用指令"));
        });

        this.manager.exceptionController().registerHandler(InvalidCommandSenderException.class, context -> {
            context.context().sender().sendMessage(MessageUtil.text("&b[服务器娘]&c只有 &e" + context.exception().requiredSenderTypes() + "&c才能这么做"));
        });
    }
}
