package com.ogclaush.playerdweller.client;

import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PlayerDwellerRenderer extends GeoEntityRenderer<PlayerDwellerEntity> {

    public PlayerDwellerRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerDwellerModel());

        this.shadowRadius = 0.35F;
    }
}