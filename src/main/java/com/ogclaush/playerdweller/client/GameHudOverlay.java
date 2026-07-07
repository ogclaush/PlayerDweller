package com.ogclaush.playerdweller.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.game.GameHudData;
import com.ogclaush.playerdweller.game.GamePhase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = PlayerDweller.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class GameHudOverlay {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {

        if (!GameHudData.visible) return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.options.hideGui) return;

        PoseStack poseStack = event.getPoseStack();
        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();

        if (GameHudData.phase == GamePhase.PREPARE) {
            float pulse = getPulse(GameHudData.seconds);

            String title = GameHudData.isDweller ? "CAÇE" : "FUJA";
            String timer = String.valueOf(GameHudData.seconds);

            int color = GameHudData.isDweller ? 0xFF3333 : 0x33FF66;

            drawCentered(poseStack, font, screenWidth, title, 8, 2.3F * pulse, color);
            drawCentered(poseStack, font, screenWidth, timer, 16, 4.2F * pulse, 0xFFFFFF);
            return;
        }

        if (GameHudData.phase == GamePhase.HUNT) {
            String timer = String.valueOf(GameHudData.seconds);

            int color = GameHudData.seconds <= 5 ? 0xFF3333 :
                    GameHudData.seconds <= 10 ? 0xFFFF55 :
                    0xFFFFFF;

            drawCentered(poseStack, font, screenWidth, timer, 10, 2.2F, color);
            return;
        }

        if (GameHudData.phase == GamePhase.VICTORY) {
            String text = GameHudData.winnerName + " venceu!";
            float pulse = 1.0F + 0.08F * (float) Math.sin(System.currentTimeMillis() / 120.0D);

            drawCentered(poseStack, font, screenWidth, text, 18, 2.6F * pulse, 0xFFFF55);
        }
    }

    private static float getPulse(int seconds) {
        long time = System.currentTimeMillis();
        float wave = (float) Math.sin(time / 90.0D);
        return 1.0F + Math.max(0.0F, wave) * 0.12F;
    }

    private static void drawCentered(PoseStack poseStack, Font font, int screenWidth, String text, int y, float scale, int color) {
        poseStack.pushPose();

        poseStack.scale(scale, scale, scale);

        int x = (int) ((screenWidth / scale - font.width(text)) / 2);

        font.drawShadow(poseStack, text, x, y, color);

        poseStack.popPose();
    }
}