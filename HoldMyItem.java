package com.holdmyitem;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = HoldMyItem.MODID, name = "Hold My Item (Backport)", version = "1.0.0",
        clientSideOnly = true, acceptedMinecraftVersions = "[1.12.2]")
public class HoldMyItem {
    public static final String MODID = "holdmyitem";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        HoldConfig.load(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new HandRenderHandler());
    }
}
