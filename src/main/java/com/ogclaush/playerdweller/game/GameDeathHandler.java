package com.ogclaush.playerdweller.game;

import com.ogclaush.playerdweller.PlayerDweller;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID)
public class GameDeathHandler {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.getServer() == null) {
            return;
        }

        DwellerGame.onPlayerDeath(player.getServer(), player);
    }
}