package net.torosamy.torosamyCore.commands;

import net.torosamy.torosamyCore.TorosamyCore;

public class CommandUtil {
    private static final CommandManager commandManager = new CommandManager(TorosamyCore.plugin);
    
    public static final AdminCommands ADMIN_COMMANDS = new AdminCommands();
    
    public static void registerCommand() {
        commandManager.annotationParser.parse(ADMIN_COMMANDS);
    }
}
