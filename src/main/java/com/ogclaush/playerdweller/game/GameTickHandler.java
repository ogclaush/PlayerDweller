package com.ogclaush.playerdweller.game;

import com.ogclaush.playerdweller.PlayerDweller;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID)
public class GameTickHandler {

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.END)
            return;

        if (ServerLifecycleHooks.getCurrentServer() == null)
            return;

        GameManager.tick(ServerLifecycleHooks.getCurrentServer());

    }

}