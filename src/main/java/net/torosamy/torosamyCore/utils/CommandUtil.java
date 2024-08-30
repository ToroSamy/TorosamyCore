package net.torosamy.torosamyCore.utils;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.commands.AdminCommands;

public class CommandUtil {
    public static void registerCommand() {
        TorosamyCore.plugin.getCommandManager().annotationParser.parse(new AdminCommands());
    }
}
