package com.ogclaush.playerdweller.commands;

import com.mojang.brigadier.Command;
import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.game.GameManager;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwellerGameCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("dwellergame")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("start")
                                .executes(ctx -> {
                                    MinecraftServer server = ctx.getSource().getServer();
                                    GameManager.start(server);
                                    return Command.SINGLE_SUCCESS;
                                }))

                        .then(Commands.literal("stop")
                                .executes(ctx -> {
                                    MinecraftServer server = ctx.getSource().getServer();
                                    GameManager.stop(server);
                                    return Command.SINGLE_SUCCESS;
                                }))

                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(
                                    Component.literal("Use /dwellergame start ou /dwellergame stop"),
                                    false
                            );
                            return Command.SINGLE_SUCCESS;
                        })
        );
    }
}