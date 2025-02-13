package com.radimous.bookmarksharing;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Bookmarksharing.MODID)
public class Bookmarksharing {
    public static final String MODID = "bookmarksharing";

    public Bookmarksharing() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

}
