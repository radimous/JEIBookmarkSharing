package com.radimous.bookmarksharing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StatusOverlay {

    private static Component textToRender;
    private static int renderUntil = 0;

    public static void renderText(Component text) {
        textToRender = text;
        var mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        renderUntil = mc.player.tickCount + 60;
    }

    public static Component getTextToRender() {
        return textToRender;
    }

    public static boolean shouldRender() {
        if (!Config.statusMessages.get()) {
            return false;
        }

        var mc = Minecraft.getInstance();
        if (mc.player == null) {
            return false;
        }
        return mc.player.tickCount < renderUntil;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGameOverlayEvent.Post event) {
        if (!shouldRender()) {
            return;
        }
        if (Minecraft.getInstance().level != null && StatusOverlay.getTextToRender() != null) {
            var window = Minecraft.getInstance().getWindow();
            GuiComponent.drawString(
                event.getMatrixStack(),
                Minecraft.getInstance().font,
                getTextToRender(),
                5,
                window.getGuiScaledHeight() - 40,
                0xFFFFFF
            );
        }
    }
}
