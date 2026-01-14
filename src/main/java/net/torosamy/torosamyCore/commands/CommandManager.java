package net.torosamy.torosamyCore.commands;


import net.torosamy.torosamyCore.config.ConfigUtil;
import net.torosamy.torosamyCore.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.InvalidSyntaxException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.bukkit.plugin.Plugin;

public class CommandManager {
    public LegacyPaperCommandManager<CommandSender> manager;
    public AnnotationParser<CommandSender> annotationParser;

    public CommandManager(Plugin plugin) {
        this.manager = LegacyPaperCommandManager.createNative(plugin, ExecutionCoordinator.simpleCoordinator());
        this.annotationParser = new AnnotationParser(this.manager, CommandManager.class);
        registerExceptionHandlers();
    }

    public void registerExceptionHandlers() {
        this.manager.exceptionController().registerHandler(InvalidSyntaxException.class, context -> {
            String correctCommand = context.exception().correctSyntax().replaceAll("\\|", "*");
            
            String message = MessageUtil.format(ConfigUtil.MAIN_CONFIG.correctCommand).replace("{command}", correctCommand);
            
            context.context().sender().sendMessage(message);
        });

        this.manager.exceptionController().registerHandler(NoPermissionException.class, context -> {
            
            String permission = context.exception().missingPermission().permissionString();
            
            String message = MessageUtil.format(ConfigUtil.MAIN_CONFIG.lackPermission.replace("{permission}", permission));
            
            context.context().sender().sendMessage(message);
        });

        this.manager.exceptionController().registerHandler(InvalidCommandSenderException.class, context -> {
            String[] split = context.exception().requiredSenderTypes().toString().split("\\.");


            for (String string : split) {
                System.out.println(string);
            }
            
            String typeString = getFormatTypeString(split[split.length - 1]);
  
            String message = MessageUtil.format(ConfigUtil.MAIN_CONFIG.commandSenderError.replace("{type}", typeString));
            
            context.context().sender().sendMessage(message);
        });
    }
    
    private String getFormatTypeString(String typeString) {
        if(typeString.equals("Player]")) {
            return ConfigUtil.MAIN_CONFIG.playerType;
        }
        if(typeString.equals("ConsoleCommandSender]")) {
            return ConfigUtil.MAIN_CONFIG.adminType;
        }
        return ConfigUtil.MAIN_CONFIG.unknownType;
    }
}
