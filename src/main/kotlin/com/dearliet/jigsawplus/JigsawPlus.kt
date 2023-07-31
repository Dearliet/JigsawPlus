package com.dearliet.jigsawplus

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.plugin.java.JavaPlugin

class JigsawPlus : JavaPlugin() {

    override fun onLoad() {
        super.onLoad()
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
        JigsawCommand.register()
        registerPacketListener()
    }

    override fun onEnable() {
        super.onEnable()
        CommandAPI.onEnable()
        saveDefaultConfig()
        globalJigsawLevel = config.getInt("globalJigsawLevel")
    }

    override fun onDisable() {
        super.onDisable()
        CommandAPI.onDisable()
        config.set("globalJigsawLevel", globalJigsawLevel)
        saveConfig()
    }

    companion object {
        var globalJigsawLevel = -1
    }

    private fun registerPacketListener(){
        ProtocolLibrary.getProtocolManager().addPacketListener(
            object : PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.JIGSAW_GENERATE) {
                override fun onPacketReceiving(event: PacketEvent) {
                    if(globalJigsawLevel >= 0) event.packet.integers.write(0, globalJigsawLevel)
                }
            }
        )
    }
}