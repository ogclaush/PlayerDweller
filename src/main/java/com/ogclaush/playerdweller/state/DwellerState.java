package com.ogclaush.playerdweller.state;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DwellerState {

    private static final Set<UUID> DWELLERS = new HashSet<>();

    public static boolean isDweller(UUID uuid) {
        return DWELLERS.contains(uuid);
    }

    public static void setDweller(UUID uuid, boolean value) {
        if (value) {
            DWELLERS.add(uuid);
        } else {
            DWELLERS.remove(uuid);
        }
    }

    public static Set<UUID> getAllDwellers() {
        return new HashSet<>(DWELLERS);
    }

    public static void clear() {
        DWELLERS.clear();
    }
}