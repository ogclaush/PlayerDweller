package com.ogclaush.playerdweller.game;

import net.minecraft.server.MinecraftServer;

public class GameManager {

    public static void start(MinecraftServer server) {
        DwellerGame.start(server);
    }

    public static void stop(MinecraftServer server) {
        DwellerGame.stop(server);
    }

    public static void tick(MinecraftServer server) {
        DwellerGame.tick(server);
    }

}