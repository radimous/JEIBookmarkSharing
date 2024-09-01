package com.radimous.bookmarksharing.mixin;

import com.radimous.bookmarksharing.Bookmarksharing;
import mezz.jei.bookmarks.BookmarkList;
import mezz.jei.gui.overlay.bookmarks.BookmarkButton;
import mezz.jei.input.UserInput;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BookmarkButton.class, remap = false)
public class MixinBookmarkButton {
    @Shadow @Final private BookmarkList bookmarkList;

    @Inject(method = "getTooltips", at = @At("TAIL"))
    private void getTooltips(List<Component> tooltip, CallbackInfo ci) {
        tooltip.add(new TextComponent("Hover over item and press CTRL+C to copy it to clipboard.").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TextComponent("Shift click on this button to import bookmark from clipboard.").withStyle(ChatFormatting.GRAY));
    }

    @Inject(method = "onMouseClicked", at = @At("HEAD"), cancellable = true)
    private void shiftClickToImport(UserInput input, CallbackInfoReturnable<Boolean> cir) {
        if (Screen.hasShiftDown()) {
            Bookmarksharing.pasteBookmark(this.bookmarkList);
            cir.setReturnValue(true);
        }
    }
}
