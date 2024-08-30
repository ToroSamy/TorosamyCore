package net.torosamy.torosamyCore

import net.torosamy.torosamyCore.manager.CommandManager
import net.torosamy.torosamyCore.utils.CommandUtil
import net.torosamy.torosamyCore.utils.ConfigUtil
import net.torosamy.torosamyCore.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.LegacyPaperCommandManager


class TorosamyCore : JavaPlugin() {
    private val commanderManager: CommandManager = CommandManager();
    companion object{lateinit var plugin: TorosamyCore}
    override fun onEnable() {
        plugin = this;
        //注册指令管理器
        commanderManager.manager = LegacyPaperCommandManager.createNative(this, ExecutionCoordinator.simpleCoordinator<CommandSender>())
        commanderManager.annotationParser = AnnotationParser<CommandSender>(commanderManager.manager, CommandSender::class.java)
        commanderManager.registerExceptionHandlers()

        CommandUtil.registerCommand()
        ConfigUtil.reloadConfig()

        Bukkit.getConsoleSender().sendMessage(MessageUtil.text("&a[服务器娘]&a插件 &eTorosamyCore &a成功开启喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.text("&a[服务器娘]&a作者 &eTorosamy|yweiyang"))
    }

    override fun onDisable() {
        ConfigUtil.saveConfig()

        Bukkit.getConsoleSender().sendMessage(MessageUtil.text("&a[服务器娘]&a插件 &eTorosamyCore &a成功开启喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.text("&a[服务器娘]&a作者 &eTorosamy|yweiyang"))
    }

    fun getCommandManager(): CommandManager {
        return this.commanderManager
    }
}