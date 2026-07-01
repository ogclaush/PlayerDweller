package com.ogclaush.playerdweller.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class PlayerDwellerEntity extends PathfinderMob implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public boolean playerMoving;
    public boolean playerSprinting;
    public boolean playerCrouching;
    public boolean playerCrawling;

    public PlayerDwellerEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        String animation;

        if (playerCrawling) {
            animation = "animation.cave_dweller.crawl";
        } else if (playerCrouching) {
            animation = playerMoving
                    ? "animation.cave_dweller.crouch_run_new"
                    : "animation.cave_dweller.crouch_idle";
        } else if (playerSprinting) {
            animation = playerMoving
                    ? "animation.cave_dweller.new_run"
                    : "animation.cave_dweller.run_idle";
        } else {
            animation = playerMoving
                    ? "animation.cave_dweller.calm_move"
                    : "animation.cave_dweller.calm_idle";
        }

        event.getController().setAnimation(
                new AnimationBuilder().addAnimation(animation)
        );

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<>(
                        this,
                        "controller",
                        2,
                        this::predicate
                )
        );
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}