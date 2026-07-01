package com.ogclaush.playerdweller.network;

import com.ogclaush.playerdweller.PlayerDweller;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PlayerDweller.MODID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void register() {
        CHANNEL.messageBuilder(SyncDwellerPacket.class, 0)
                .encoder(SyncDwellerPacket::encode)
                .decoder(SyncDwellerPacket::decode)
                .consumerMainThread(SyncDwellerPacket::handle)
                .add();
    }
}