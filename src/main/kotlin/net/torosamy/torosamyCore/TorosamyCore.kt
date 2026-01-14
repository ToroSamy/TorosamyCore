package net.torosamy.torosamyCore

import net.torosamy.torosamyCore.commands.CommandUtil
import net.torosamy.torosamyCore.config.ConfigUtil
import net.torosamy.torosamyCore.holder.HandItemHolder
import net.torosamy.torosamyCore.holder.NearbyHolder
import net.torosamy.torosamyCore.holder.RandomNumberHolder
import net.torosamy.torosamyCore.holder.TimeHolder
import net.torosamy.torosamyCore.inventory.InventoryBlocker
import net.torosamy.torosamyCore.utils.MessageUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class TorosamyCore : JavaPlugin() {
    companion object{
        lateinit var plugin: TorosamyCore
        val RANDOM_NUMBER_HOLDER: RandomNumberHolder = RandomNumberHolder()
        val NEARBY_HOLDER: NearbyHolder = NearbyHolder()
        val TIME_HOLDER: TimeHolder = TimeHolder()
        val HAND_ITEM_HOLDER: HandItemHolder = HandItemHolder()
    }
    override fun onEnable() {
        plugin = this
        CommandUtil.registerCommand()
        
        ConfigUtil.initConfig()
        ConfigUtil.reloadConfig()
        server.pluginManager.registerEvents(InventoryBlocker(), this)
        RANDOM_NUMBER_HOLDER.register()
        NEARBY_HOLDER.register()
        TIME_HOLDER.register()
        HAND_ITEM_HOLDER.register()
        
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&a插件 &eTorosamyCore &a成功开启喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&a作者 &eTorosamy|yweiyang"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&aNBT &ev1_21_R5"))
    }

    override fun onDisable() {
        ConfigUtil.saveConfig()

        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c插件 &eTorosamyCore &c成功关闭喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&b[服务器娘]&c作者 &eTorosamy|yweiyang"))
    }
}