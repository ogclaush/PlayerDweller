package com.ogclaush.playerdweller.game;

import com.ogclaush.playerdweller.network.PacketHandler;
import com.ogclaush.playerdweller.network.SyncDwellerPacket;
import com.ogclaush.playerdweller.network.SyncGameHudPacket;
import com.ogclaush.playerdweller.state.DwellerAttributes;
import com.ogclaush.playerdweller.state.DwellerState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DwellerGame {

    public static final int HUNT_TIME = 30 * 20;
    public static final int VICTORY_TIME = 5 * 20;

    private static boolean running = false;
    private static boolean stopAfterVictory = false;

    private static GamePhase phase = GamePhase.IDLE;
    private static int timer = 0;

    private static UUID dweller;
    private static UUID human;

    private static String winnerName = "";

    private static int prepareSeconds = 5;
    private static int survivedRounds = 0;

    public static void start(MinecraftServer server) {
        if (running) {
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("Dweller Game já está rodando."),
                    false
            );
            return;
        }

        List<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());

        if (players.size() < 2) {
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("Precisa de pelo menos 2 jogadores para iniciar."),
                    false
            );
            return;
        }

        Collections.shuffle(players);

        ServerPlayer dwellerPlayer = players.get(0);
        ServerPlayer humanPlayer = players.get(1);

        dweller = dwellerPlayer.getUUID();
        human = humanPlayer.getUUID();

        running = true;
        stopAfterVictory = false;

        phase = GamePhase.PREPARE;

        prepareSeconds = 5;
        survivedRounds = 0;
        timer = prepareSeconds * 20;

        winnerName = "";

        applyRoles(server);
        applyPrepareEffects(server);
        syncHud(server);

        server.getPlayerList().broadcastSystemMessage(
                Component.literal("Dweller Game iniciado!"),
                false
        );
    }

    public static void stop(MinecraftServer server) {
        if (!running) {
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("Dweller Game não está rodando."),
                    false
            );
            return;
        }

        resetPlayer(server, dweller);
        resetPlayer(server, human);

        running = false;
        stopAfterVictory = false;

        phase = GamePhase.IDLE;
        timer = 0;

        dweller = null;
        human = null;
        winnerName = "";

        prepareSeconds = 5;
        survivedRounds = 0;

        hideHud(server);

        server.getPlayerList().broadcastSystemMessage(
                Component.literal("Dweller Game encerrado."),
                false
        );
    }

    public static void tick(MinecraftServer server) {
        if (!running) {
            return;
        }

        if (phase == GamePhase.VICTORY && timer % 10 == 0) {
            spawnWinnerFirework(server);
        }

        timer--;
        syncHud(server);

        if (timer > 0) {
            return;
        }

        if (phase == GamePhase.PREPARE) {
            phase = GamePhase.HUNT;
            timer = HUNT_TIME;
            winnerName = "";

            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("A caçada começou!"),
                    false
            );

            syncHud(server);
            return;
        }

        if (phase == GamePhase.HUNT) {
            survivedRounds++;

            if (survivedRounds % 2 == 0 && prepareSeconds > 0) {
                prepareSeconds--;
            }

            swapRoles(server);

            phase = GamePhase.PREPARE;
            timer = prepareSeconds * 20;
            winnerName = "";
            stopAfterVictory = false;

            applyPrepareEffects(server);
            syncHud(server);

            if (timer <= 0) {
                phase = GamePhase.HUNT;
                timer = HUNT_TIME;
                syncHud(server);
            }

            return;
        }

        if (phase == GamePhase.VICTORY) {
            if (stopAfterVictory) {
                stop(server);
            }
        }
    }

    public static void onPlayerDeath(MinecraftServer server, ServerPlayer player) {
        if (!running) {
            return;
        }

        if (phase != GamePhase.HUNT) {
            return;
        }

        if (!player.getUUID().equals(human)) {
            return;
        }

        ServerPlayer dwellerPlayer = server.getPlayerList().getPlayer(dweller);

        winnerName = dwellerPlayer != null
                ? dwellerPlayer.getName().getString()
                : "Cave Dweller";

        stopAfterVictory = true;

        phase = GamePhase.VICTORY;
        timer = VICTORY_TIME;

        server.getPlayerList().broadcastSystemMessage(
                Component.literal(winnerName + " venceu!"),
                false
        );

        syncHud(server);
    }

    private static void swapRoles(MinecraftServer server) {
        UUID oldDweller = dweller;

        dweller = human;
        human = oldDweller;

        applyRoles(server);

        server.getPlayerList().broadcastSystemMessage(
                Component.literal("Os papéis foram invertidos!"),
                false
        );
    }

    private static void applyRoles(MinecraftServer server) {
        setDweller(server, dweller, true);
        setDweller(server, human, false);

        ServerPlayer dwellerPlayer = server.getPlayerList().getPlayer(dweller);
        ServerPlayer humanPlayer = server.getPlayerList().getPlayer(human);

        if (dwellerPlayer != null) {
            DwellerAttributes.applyDweller(dwellerPlayer);
        }

        if (humanPlayer != null) {
            DwellerAttributes.applyHuman(humanPlayer);
        }
    }

    private static void applyPrepareEffects(MinecraftServer server) {
        int prepareTicks = prepareSeconds * 20;

        if (prepareTicks <= 0) {
            return;
        }

        ServerPlayer dwellerPlayer = server.getPlayerList().getPlayer(dweller);
        ServerPlayer humanPlayer = server.getPlayerList().getPlayer(human);

        if (dwellerPlayer != null) {
            dwellerPlayer.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    prepareTicks,
                    10,
                    false,
                    true
            ));

            dwellerPlayer.addEffect(new MobEffectInstance(
                    MobEffects.BLINDNESS,
                    prepareTicks,
                    0,
                    false,
                    true
            ));
        }

        if (humanPlayer != null) {
            humanPlayer.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED,
                    prepareTicks,
                    1,
                    false,
                    true
            ));
        }
    }

    private static void spawnWinnerFirework(MinecraftServer server) {
        ServerPlayer winner = server.getPlayerList().getPlayer(dweller);

        if (winner == null) {
            return;
        }

        if (!(winner.level instanceof ServerLevel level)) {
            return;
        }

        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);

        CompoundTag fireworksTag = new CompoundTag();
        fireworksTag.putByte("Flight", (byte) 1);

        CompoundTag explosionTag = new CompoundTag();
        explosionTag.putByte("Type", (byte) 1);
        explosionTag.putIntArray("Colors", new int[]{0xFF3333, 0xFFFFFF});
        explosionTag.putIntArray("FadeColors", new int[]{0xFFFF55});
        explosionTag.putBoolean("Flicker", true);
        explosionTag.putBoolean("Trail", true);

        ListTag explosions = new ListTag();
        explosions.add(explosionTag);

        fireworksTag.put("Explosions", explosions);
        stack.getOrCreateTag().put("Fireworks", fireworksTag);

        FireworkRocketEntity firework = new FireworkRocketEntity(
                level,
                winner.getX(),
                winner.getY() + 1.0D,
                winner.getZ(),
                stack
        );

        level.addFreshEntity(firework);
    }

    private static void resetPlayer(MinecraftServer server, UUID uuid) {
        if (uuid == null) {
            return;
        }

        setDweller(server, uuid, false);

        ServerPlayer player = server.getPlayerList().getPlayer(uuid);

        if (player != null) {
            DwellerAttributes.applyHuman(player);
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.removeEffect(MobEffects.MOVEMENT_SPEED);
            player.removeEffect(MobEffects.GLOWING);
        }
    }

    private static void setDweller(MinecraftServer server, UUID uuid, boolean value) {
        if (uuid == null) {
            return;
        }

        DwellerState.setDweller(uuid, value);

        PacketHandler.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new SyncDwellerPacket(uuid, value)
        );
    }

    private static void syncHud(MinecraftServer server) {
        int seconds = Math.max(0, (timer + 19) / 20);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            boolean isDweller = player.getUUID().equals(dweller);

            PacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncGameHudPacket(true, phase, seconds, isDweller, winnerName)
            );
        }
    }

    private static void hideHud(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncGameHudPacket(false, GamePhase.IDLE, 0, false, "")
            );;
        }
    }
}