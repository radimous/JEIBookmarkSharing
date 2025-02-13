package com.radimous.bookmarksharing;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.common.util.Translator;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Bookmarksharing.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Keybinds {
    public static final KeyMapping copyBookmark = new KeyMapping("key.bookmarksharing.copybookmark", KeyConflictContext.GUI, KeyModifier.CONTROL, InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_C), Translator.translateToLocal("jei.key.category.mouse.hover"));

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(copyBookmark);
    }
}
