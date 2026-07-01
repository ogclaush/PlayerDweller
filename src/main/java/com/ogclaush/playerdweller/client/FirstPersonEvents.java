package com.ogclaush.playerdweller.client;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = PlayerDweller.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class FirstPersonEvents {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        if (DwellerState.isDweller(mc.player.getUUID())) {
            event.setCanceled(true);
        }
    }
}