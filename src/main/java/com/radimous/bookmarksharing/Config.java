package com.radimous.bookmarksharing;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue statusMessages = builder
        .comment("Show status messages when copying/pasting to/from clipboard")
        .define("statusMessages", true);
    public static final ForgeConfigSpec CLIENT_SPEC = builder.build();
}