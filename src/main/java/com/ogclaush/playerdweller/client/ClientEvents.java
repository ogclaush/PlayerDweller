package com.ogclaush.playerdweller.client;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.ModEntities;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = PlayerDweller.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class ClientEvents {

    private static final Map<UUID, PlayerDwellerEntity> DWELLERS = new HashMap<>();

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {

        Player rawPlayer = event.getEntity();

        if (!(rawPlayer instanceof AbstractClientPlayer player)) {
            return;
        }

        if (!DwellerState.isDweller(player.getUUID())) {
            return;
        }

        event.setCanceled(true);

        Minecraft mc = Minecraft.getInstance();

        PlayerDwellerEntity dweller = DWELLERS.computeIfAbsent(player.getUUID(), uuid ->
                new PlayerDwellerEntity(
                        ModEntities.PLAYER_DWELLER.get(),
                        mc.level
                )
        );

        dweller.setPos(player.getX(), player.getY(), player.getZ());

        dweller.setYRot(player.getYRot());
        dweller.setXRot(player.getXRot());

        dweller.yRotO = player.yRotO;
        dweller.xRotO = player.xRotO;

        dweller.yBodyRot = player.yBodyRot;
        dweller.yBodyRotO = player.yBodyRotO;

        dweller.yHeadRot = player.yHeadRot;
        dweller.yHeadRotO = player.yHeadRotO;

        dweller.setDeltaMovement(player.getDeltaMovement());

        double speed = player.getDeltaMovement().horizontalDistance();

        dweller.playerMoving = speed > 0.01D;
        dweller.playerSprinting = player.isSprinting();
        dweller.playerCrouching = player.isCrouching();
        dweller.playerCrawling = player.isSwimming() || player.isFallFlying();

        dweller.tickCount = player.tickCount;

        EntityRenderer<? super PlayerDwellerEntity> renderer =
                mc.getEntityRenderDispatcher().getRenderer(dweller);

        renderer.render(
                dweller,
                player.getYRot(),
                event.getPartialTick(),
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight()
        );
    }
}