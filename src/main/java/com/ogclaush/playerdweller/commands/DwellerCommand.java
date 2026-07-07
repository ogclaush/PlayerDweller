package com.ogclaush.playerdweller.commands;

import com.mojang.brigadier.Command;
import com.ogclaush.playerdweller.PlayerDweller;
import com.ogclaush.playerdweller.network.PacketHandler;
import com.ogclaush.playerdweller.network.SyncDwellerPacket;
import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = PlayerDweller.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwellerCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("dweller")
                        .executes(ctx -> toggle(ctx.getSource().getPlayerOrException()))
                        .then(
                                Commands.argument("player", EntityArgument.player())
                                        .requires(source -> source.hasPermission(2))
                                        .executes(ctx -> toggle(EntityArgument.getPlayer(ctx, "player")))
                        )
        );
    }

    private static int toggle(ServerPlayer player) {

        boolean enabled = !DwellerState.isDweller(player.getUUID());

        DwellerState.setDweller(player.getUUID(), enabled);

        PacketHandler.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new SyncDwellerPacket(player.getUUID(), enabled)
        );

        AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance damage = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance stepHeight = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());

        if (enabled) {

            if (health != null) health.setBaseValue(60.0D);
            if (speed != null) speed.setBaseValue(0.18D);
            if (damage != null) damage.setBaseValue(12.0D);

            // Sobe blocos automaticamente, tipo cavalo.
            if (stepHeight != null) stepHeight.setBaseValue(1.0D);

            player.setHealth(60.0F);

        } else {

            if (health != null) health.setBaseValue(20.0D);
            if (speed != null) speed.setBaseValue(0.10D);
            if (damage != null) damage.setBaseValue(1.0D);

            // Volta ao normal.
            if (stepHeight != null) stepHeight.setBaseValue(0.0D);

            if (player.getHealth() > 20.0F) {
                player.setHealth(20.0F);
            }
        }

        player.displayClientMessage(
                Component.literal(enabled
                        ? "Modo Dweller ativado!"
                        : "Modo Dweller desativado!"),
                false
        );

        return Command.SINGLE_SUCCESS;
    }
}