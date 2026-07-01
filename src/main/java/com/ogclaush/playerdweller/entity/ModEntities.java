package com.ogclaush.playerdweller.entity;

import com.ogclaush.playerdweller.PlayerDweller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PlayerDweller.MODID);

    public static final RegistryObject<EntityType<PlayerDwellerEntity>> PLAYER_DWELLER =
            ENTITIES.register("player_dweller",
                    () -> EntityType.Builder
                            .of(PlayerDwellerEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 2.9F)
                            .clientTrackingRange(8)
                            .build("player_dweller"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
