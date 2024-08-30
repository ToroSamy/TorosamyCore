package net.torosamy.torosamyCore.manager;


import net.torosamy.torosamyCore.utils.ConfigUtil;
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
            String correctCommand = context.exception().correctSyntax().replaceAll("\\|", "*");
            context.context().sender().sendMessage(MessageUtil.text(ConfigUtil.langConfig.correctCommand).replace("{command}", correctCommand));
        });

        this.manager.exceptionController().registerHandler(NoPermissionException.class, context -> {
            context.context().sender().sendMessage(MessageUtil.text(ConfigUtil.langConfig.lackPermission.replace("{permission}",context.exception().missingPermission().permissionString())));
        });

        this.manager.exceptionController().registerHandler(InvalidCommandSenderException.class, context -> {
            String[] split = context.exception().requiredSenderTypes().toString().split("\\.");
            String typeString = split[split.length - 1];
            if(typeString.equals("Player]")) typeString = ConfigUtil.langConfig.playerType;
            else if(typeString.equals("ConsoleCommandSender]")) typeString=ConfigUtil.langConfig.adminType;
            else typeString = ConfigUtil.langConfig.unknownType;
            context.context().sender().sendMessage(MessageUtil.text(ConfigUtil.langConfig.commandSenderError.replace("{type}", typeString)));
        });
    }
}
