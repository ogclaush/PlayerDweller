package com.ogclaush.playerdweller.events;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.network.PacketHandler;
import com.ogclaush.playerdweller.network.SyncDwellerPacket;
import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSyncEvents {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        for (UUID uuid : DwellerState.getAllDwellers()) {
            PacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncDwellerPacket(uuid, true)
            );
        }
    }
}