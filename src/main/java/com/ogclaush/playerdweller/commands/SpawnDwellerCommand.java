package com.ogclaush.playerdweller.commands;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.ModEntities;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnDwellerCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("spawnplayerdweller")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Level level = player.level;

                            PlayerDwellerEntity dweller = ModEntities.PLAYER_DWELLER.get().create(level);

                            if (dweller == null) {
                                return 0;
                            }

                            dweller.moveTo(
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    player.getYRot(),
                                    player.getXRot()
                            );

                            level.addFreshEntity(dweller);

                            return 1;
                        })
        );
    }
}