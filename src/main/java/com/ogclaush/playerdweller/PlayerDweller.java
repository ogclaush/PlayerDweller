package com.ogclaush.playerdweller;

import com.ogclaush.playerdweller.entity.ModEntities;
import com.ogclaush.playerdweller.network.PacketHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PlayerDweller.MODID)
public class PlayerDweller {

    public static final String MODID = "playerdweller";

    public PlayerDweller() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(modBus);
        PacketHandler.register();
    }
}