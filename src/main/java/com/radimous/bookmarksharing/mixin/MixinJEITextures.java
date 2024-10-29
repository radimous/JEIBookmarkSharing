package com.radimous.bookmarksharing.mixin;

import com.radimous.bookmarksharing.BookmarkImportIcon;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.gui.elements.DrawableSprite;
import mezz.jei.gui.textures.Textures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Textures.class, remap = false)
public abstract class MixinJEITextures implements BookmarkImportIcon {
    @Shadow protected abstract DrawableSprite registerGuiSprite(String name, int width, int height);

    private IDrawableStatic bookmarkImportButtonIcon;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        bookmarkImportButtonIcon =  this.registerGuiSprite("icons/bookmark_import_button", 16, 16);
    }

    @Override public IDrawableStatic getBookmarkImportIcon() {
        return bookmarkImportButtonIcon;
    }
}
