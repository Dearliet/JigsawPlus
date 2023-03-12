package com.dearliet.jigsawplus

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.plugin.java.JavaPlugin

class JigsawPlus : JavaPlugin() {

    override fun onLoad() {
        super.onLoad()
        JigsawCommand.register()
        registerPacketListener()
    }

    override fun onEnable() {
        super.onEnable()
        saveDefaultConfig()
        globalJigsawLevel = config.getInt("globalJigsawLevel")
    }

    override fun onDisable() {
        super.onDisable()
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