package com.ogclaush.playerdweller.events;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.ModEntities;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = PlayerDweller.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.PLAYER_DWELLER.get(),
                PlayerDwellerEntity.createAttributes().build()
        );
    }
}