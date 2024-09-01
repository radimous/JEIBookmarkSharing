package com.radimous.bookmarksharing.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.radimous.bookmarksharing.Bookmarksharing;
import mezz.jei.common.util.Translator;
import mezz.jei.input.CombinedRecipeFocusSource;
import mezz.jei.input.UserInput;
import mezz.jei.input.mouse.IUserInputHandler;
import mezz.jei.input.mouse.handlers.BookmarkInputHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
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
    private void copyToClipboard(Screen screen, UserInput input,
                                 CallbackInfoReturnable<Optional<IUserInputHandler>> cir) {
        var ctrlC = new KeyMapping("key.jei.bookmark", KeyConflictContext.GUI, KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM.getOrCreate(InputConstants.KEY_C),
            Translator.translateToLocal("jei.key.category.mouse.hover"));

        if (input.is(ctrlC)) {
            this.focusSource.getIngredientUnderMouse(input).findFirst().flatMap((clicked) -> {
                Bookmarksharing.copyItemToClipboard(clicked.getTypedIngredient());
                return Optional.empty();
            });

            cir.setReturnValue(Optional.empty());
        }
    }
}
