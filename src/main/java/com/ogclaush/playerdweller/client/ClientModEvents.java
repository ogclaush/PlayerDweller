package com.ogclaush.playerdweller.client;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = PlayerDweller.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientModEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            EntityRenderers.register(
                    ModEntities.PLAYER_DWELLER.get(),
                    PlayerDwellerRenderer::new
            );

        });

    }
}