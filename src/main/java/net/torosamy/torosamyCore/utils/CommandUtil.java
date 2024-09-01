package net.torosamy.torosamyCore.utils;

import net.torosamy.torosamyCore.TorosamyCore;
import net.torosamy.torosamyCore.commands.AdminCommands;

public class CommandUtil {
    public static void registerCommand() {
        TorosamyCore.commanderManager.annotationParser.parse(new AdminCommands());
    }
}
