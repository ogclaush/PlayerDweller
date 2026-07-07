package com.ogclaush.playerdweller.network;

import com.ogclaush.playerdweller.game.GameHudData;
import com.ogclaush.playerdweller.game.GamePhase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncGameHudPacket {

    private final boolean visible;
    private final GamePhase phase;
    private final int seconds;
    private final boolean isDweller;
    private final String winnerName;

    public SyncGameHudPacket(boolean visible, GamePhase phase, int seconds, boolean isDweller, String winnerName) {
        this.visible = visible;
        this.phase = phase;
        this.seconds = seconds;
        this.isDweller = isDweller;
        this.winnerName = winnerName;
    }

    public static void encode(SyncGameHudPacket packet, FriendlyByteBuf buf) {
        buf.writeBoolean(packet.visible);
        buf.writeEnum(packet.phase);
        buf.writeInt(packet.seconds);
        buf.writeBoolean(packet.isDweller);
        buf.writeUtf(packet.winnerName);
    }

    public static SyncGameHudPacket decode(FriendlyByteBuf buf) {
        return new SyncGameHudPacket(
                buf.readBoolean(),
                buf.readEnum(GamePhase.class),
                buf.readInt(),
                buf.readBoolean(),
                buf.readUtf()
        );
    }

    public static void handle(SyncGameHudPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            GameHudData.visible = packet.visible;
            GameHudData.phase = packet.phase;
            GameHudData.seconds = packet.seconds;
            GameHudData.isDweller = packet.isDweller;
            GameHudData.winnerName = packet.winnerName;
        });

        ctx.get().setPacketHandled(true);
    }
}