package com.ogclaush.playerdweller.state;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class DwellerAttributes {

    public static void applyDweller(ServerPlayer player) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance stepHeight = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

        if (health != null) health.setBaseValue(60.0D);
        if (speed != null) speed.setBaseValue(0.18D);
        if (damage != null) damage.setBaseValue(12.0D);
        if (stepHeight != null) stepHeight.setBaseValue(1.0D);

        player.setHealth(60.0F);

        player.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                999999,
                0,
                false,
                false
        ));
    }

    public static void applyHuman(ServerPlayer player) {
        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance stepHeight = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

        if (health != null) health.setBaseValue(20.0D);
        if (speed != null) speed.setBaseValue(0.10D);
        if (damage != null) damage.setBaseValue(1.0D);
        if (stepHeight != null) stepHeight.setBaseValue(0.0D);

        player.removeEffect(MobEffects.GLOWING);

        if (player.getHealth() > 20.0F) {
            player.setHealth(20.0F);
        }
    }
}