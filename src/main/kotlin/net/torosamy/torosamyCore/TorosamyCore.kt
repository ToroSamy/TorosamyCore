package net.torosamy.torosamyCore

import net.torosamy.torosamyCore.manager.CommandManager
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager


class TorosamyCore : JavaPlugin() {
    private val commanderManager: CommandManager = CommandManager();
    override fun onEnable() {
        //注册指令管理器
        commanderManager.manager = LegacyPaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator<CommandSender>())
        commanderManager.annotationParser = AnnotationParser<CommandSender>(commanderManager.manager, CommandSender::class.java)
        commanderManager.registerExceptionHandlers()
    }

    override fun onDisable() {
        logger.info("Torosamy Core Disabled")
    }

    fun getCommandManager(): CommandManager {
        return this.commanderManager
    }
}