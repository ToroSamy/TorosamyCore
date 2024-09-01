package net.torosamy.torosamyCore

import net.torosamy.torosamyCore.manager.CommandManager
import net.torosamy.torosamyCore.utils.CommandUtil
import net.torosamy.torosamyCore.utils.ConfigUtil
import net.torosamy.torosamyCore.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class TorosamyCore : JavaPlugin() {
    companion object{
        lateinit var plugin: TorosamyCore
        lateinit var commanderManager: CommandManager
    }
    override fun onEnable() {
        plugin = this;
        commanderManager = CommandManager(this)

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
}