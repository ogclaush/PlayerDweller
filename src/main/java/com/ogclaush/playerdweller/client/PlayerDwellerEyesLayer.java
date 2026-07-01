package com.ogclaush.playerdweller.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.entity.PlayerDwellerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class PlayerDwellerEyesLayer extends GeoLayerRenderer<PlayerDwellerEntity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(PlayerDweller.MODID,
                    "textures/entity/player_dweller_eyes.png");

    public PlayerDwellerEyesLayer(IGeoRenderer<PlayerDwellerEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack matrixStackIn,
                       MultiBufferSource bufferIn,
                       int packedLightIn,
                       PlayerDwellerEntity entityLivingBaseIn,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        packedLightIn = 15728880;

        RenderType eyesRenderType = RenderType.eyes(TEXTURE);

        VertexConsumer vertexConsumer =
                bufferIn.getBuffer(eyesRenderType);

        this.getRenderer().render(
                this.getEntityModel().getModel(
                        this.getEntityModel().getModelResource(entityLivingBaseIn)
                ),
                entityLivingBaseIn,
                partialTicks,
                eyesRenderType,
                matrixStackIn,
                bufferIn,
                vertexConsumer,
                packedLightIn,
                OverlayTexture.NO_OVERLAY,
                1F, 1F, 1F, 1F
        );
    }
}