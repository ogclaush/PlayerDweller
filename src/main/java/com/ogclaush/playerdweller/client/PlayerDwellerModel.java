package com.ogclaush.playerdweller.client;

import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PlayerDwellerModel extends AnimatedGeoModel<PlayerDwellerEntity> {

    @Override
    public ResourceLocation getModelResource(PlayerDwellerEntity object) {
        return new ResourceLocation(PlayerDweller.MODID, "geo/player_dweller.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlayerDwellerEntity object) {
        return new ResourceLocation(PlayerDweller.MODID, "textures/entity/player_dweller.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlayerDwellerEntity animatable) {
        return new ResourceLocation(PlayerDweller.MODID, "animations/player_dweller.animation.json");
    }
}