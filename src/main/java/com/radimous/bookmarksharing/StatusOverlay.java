package com.radimous.bookmarksharing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class StatusOverlay {

    private static Component textToRender;
    private static int renderUntil = 0;
    private static final int RENDER_DURATION = 60;

    public static void renderText(Component text) {
        textToRender = text;
        var mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        renderUntil = mc.player.tickCount + RENDER_DURATION;
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
        if (renderUntil - mc.player.tickCount > RENDER_DURATION) {
            // switching worlds
            renderUntil = 0;
        }
        return mc.player.tickCount < renderUntil;
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
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
