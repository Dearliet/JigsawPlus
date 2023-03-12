package com.dearliet.jigsawplus

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ExecutorType
import org.bukkit.ChatColor

object JigsawCommand {

    fun register(){
        val set = CommandAPICommand("set")
                .withArguments(IntegerArgument("value"))
                .executes(CommandExecutor { sender, args ->
                    val value = args[0] as Int

                    if(value < 0){
                        sender.sendMessage("${ChatColor.RED}Value must be positive")
                        return@CommandExecutor
                    }

                    JigsawPlus.globalJigsawLevel = value
                    sender.sendMessage("The global jigsaw level has been set to $value")
                })

        val get = CommandAPICommand("get")
                .executes(CommandExecutor { sender, _ ->
                    sender.sendMessage("The global jigsaw level is ${if(JigsawPlus.globalJigsawLevel < 0) "undefined" else JigsawPlus.globalJigsawLevel}")
                }, ExecutorType.PLAYER, ExecutorType.CONSOLE)

        val clear = CommandAPICommand("clear")
                .executes(CommandExecutor { sender, _ ->
                    if(JigsawPlus.globalJigsawLevel < 0){
                        sender.sendMessage("${ChatColor.RED}The global jigsaw level has already been cleared")
                        return@CommandExecutor
                    }

                    sender.sendMessage("The global jigsaw level has been cleared")
                    sender.sendMessage("Jigsaws will use their default interface value")
                    JigsawPlus.globalJigsawLevel = -1
                }, ExecutorType.PLAYER, ExecutorType.CONSOLE)

        val levels = CommandAPICommand("levels")
                .withSubcommand(get)
                .withSubcommand(set)
                .withSubcommand(clear)

        CommandAPICommand("jigsaw")
                .withSubcommand(levels)
                .register()
    }
}