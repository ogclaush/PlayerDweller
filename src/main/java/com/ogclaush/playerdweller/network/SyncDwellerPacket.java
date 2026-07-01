package com.ogclaush.playerdweller.network;

import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncDwellerPacket {

    private final UUID uuid;
    private final boolean enabled;

    public SyncDwellerPacket(UUID uuid, boolean enabled) {
        this.uuid = uuid;
        this.enabled = enabled;
    }

    public static void encode(SyncDwellerPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.uuid);
        buf.writeBoolean(packet.enabled);
    }

    public static SyncDwellerPacket decode(FriendlyByteBuf buf) {
        return new SyncDwellerPacket(buf.readUUID(), buf.readBoolean());
    }

    public static void handle(SyncDwellerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DwellerState.setDweller(packet.uuid, packet.enabled);
        });

        ctx.get().setPacketHandled(true);
    }
}