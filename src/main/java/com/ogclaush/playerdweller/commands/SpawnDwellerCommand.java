package com.ogclaush.playerdweller.commands;

import com.ogclaush.playerdweller.entity.ModEntities;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SpawnDwellerCommand {

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("spawnplayerdweller")
                        .requires(source -> source.hasPermission(2))
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