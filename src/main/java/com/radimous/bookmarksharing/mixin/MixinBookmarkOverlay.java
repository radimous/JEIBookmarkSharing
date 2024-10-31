package com.radimous.bookmarksharing.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.radimous.bookmarksharing.BookmarkImportIcon;
import com.radimous.bookmarksharing.Bookmarksharing;
import mezz.jei.bookmarks.BookmarkList;
import mezz.jei.gui.GuiScreenHelper;
import mezz.jei.gui.TooltipRenderer;
import mezz.jei.gui.elements.GuiIconButton;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import mezz.jei.gui.textures.Textures;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.input.mouse.handlers.CombinedInputHandler;
import mezz.jei.common.network.IConnectionToServer;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.core.config.IClientConfig;
import mezz.jei.core.config.IWorldConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(value = BookmarkOverlay.class, remap = false)
public abstract class MixinBookmarkOverlay {
    @Shadow private ImmutableRect2i parentArea;
    @Unique private GuiIconButton bookmarkImportButton;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BookmarkList bookmarkList, Textures textures, IngredientGridWithNavigation contents,
                      IClientConfig clientConfig, IWorldConfig worldConfig, GuiScreenHelper guiScreenHelper,
                      IConnectionToServer serverConnection, CallbackInfo ci) {
        this.bookmarkImportButton = new GuiIconButton(((BookmarkImportIcon)textures).getBookmarkImportIcon(), b -> Bookmarksharing.pasteBookmark(bookmarkList));
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void drawScreen(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, float partialTicks,
                            CallbackInfo ci) {
        this.bookmarkImportButton.render(poseStack,mouseX, mouseY, partialTicks);
    }

    @Inject(method = "drawTooltips", at = @At("RETURN"))
    private void drawTooltips(Minecraft minecraft, PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci) {
        if (bookmarkImportButton.isMouseOver(mouseX, mouseY)) {
            List<Component> tooltip = List.of(
                new TextComponent("Import bookmark from clipboard"),
                new TextComponent("Hover over item and press CTRL+C to copy it to clipboard.").withStyle(ChatFormatting.GRAY));
            TooltipRenderer.drawHoveringText(poseStack, tooltip, mouseX, mouseY);
        }
    }

    @Inject(method = "updateBounds(Ljava/util/Set;)Z", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/elements/GuiIconToggleButton;updateBounds(Lmezz/jei/common/util/ImmutableRect2i;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void updateBounds(Set<ImmutableRect2i> guiExclusionAreas, CallbackInfoReturnable<Boolean> cir,
                              ImmutableRect2i availableContentsArea, boolean contentsHasRoom,
                              ImmutableRect2i contentsArea, ImmutableRect2i bookmarkButtonArea) {
        // 4 pixel gap
        ImmutableRect2i bookmarkImportButtonArea = this.parentArea.matchWidthAndX(contentsArea).keepBottom(20).keepLeft(20).moveRight(24);
        this.bookmarkImportButton.updateBounds(bookmarkImportButtonArea);
    }

    @ModifyVariable(method = "createInputHandler", name = "bookmarkButtonInputHandler", at = @At(value = "STORE", ordinal = 0), remap = false)
    public IUserInputHandler createInputHandler(IUserInputHandler value) {
        return new CombinedInputHandler(this.bookmarkImportButton.createInputHandler(), value);
    }
}
