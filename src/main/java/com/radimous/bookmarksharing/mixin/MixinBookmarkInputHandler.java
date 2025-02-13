package com.radimous.bookmarksharing.mixin;

import com.radimous.bookmarksharing.Bookmarksharing;
import com.radimous.bookmarksharing.Keybinds;
import mezz.jei.common.input.IKeyBindings;
import mezz.jei.common.input.CombinedRecipeFocusSource;
import mezz.jei.common.input.UserInput;
import mezz.jei.common.input.IUserInputHandler;
import mezz.jei.common.input.handlers.BookmarkInputHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(value = BookmarkInputHandler.class, remap = false)
public abstract class MixinBookmarkInputHandler {
    @Final @Shadow private CombinedRecipeFocusSource focusSource;

    @Inject(method = "handleUserInput", at = @At("HEAD"), cancellable = true)
    private void copyToClipboard(Screen screen, UserInput input, IKeyBindings keyBindings, CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
        if (input.is(Keybinds.copyBookmark)) {
            this.focusSource.getIngredientUnderMouse(input, keyBindings).findFirst().flatMap((clicked) -> {
                Bookmarksharing.copyItemToClipboard(clicked.getTypedIngredient());
                return Optional.empty();
            });

            cir.setReturnValue(Optional.empty());
        }
    }
}
