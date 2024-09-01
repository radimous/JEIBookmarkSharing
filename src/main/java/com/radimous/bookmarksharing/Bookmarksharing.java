package com.radimous.bookmarksharing;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.bookmarks.BookmarkList;
import mezz.jei.common.ingredients.IngredientInfo;
import mezz.jei.common.ingredients.RegisteredIngredients;
import mezz.jei.common.ingredients.TypedIngredient;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static mezz.jei.common.plugins.debug.JeiDebugPlugin.jeiRuntime;

@Mod("bookmarksharing")
public class Bookmarksharing {
    public static final Logger LOGGER = LogUtils.getLogger();

    public Bookmarksharing() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }
    public static void copyItemToClipboard(ITypedIngredient<?> ingredient) {
        Optional<ItemStack> itemStack = ingredient.getIngredient(VanillaTypes.ITEM_STACK);
        if (itemStack.isEmpty()) {
            return;
        }
        ItemStack is = itemStack.get();
        String snbt = is.serializeNBT().toString();

        KeyboardHandler clipboard = Minecraft.getInstance().keyboardHandler;
        clipboard.setClipboard(snbt);

        MutableComponent cmp = new TextComponent("Copied ").append(is.getDisplayName()).append(" to clipboard.");
        StatusOverlay.renderText(cmp);
    }

    public static void pasteBookmark(BookmarkList bookmarkList) {
        KeyboardHandler clipboard = Minecraft.getInstance().keyboardHandler;
        String snbt = clipboard.getClipboard();
        ItemStack newStack;
        try {
            CompoundTag serialized = TagParser.parseTag(snbt);
            newStack = ItemStack.of(serialized);
        } catch (CommandSyntaxException e) {
            MutableComponent cmp = new TextComponent("Couldn't import bookmark from clipboard. Invalid format.");
            StatusOverlay.renderText(cmp);
            return;
        }
        IJeiRuntime jr = jeiRuntime;
        if (jr == null) {
            MutableComponent cmp = new TextComponent("Couldn't import bookmark from clipboard. Failed to create JEI Ingredient.");
            StatusOverlay.renderText(cmp);
            return;
        }

        IIngredientManager im = jr.getIngredientManager();
        IngredientInfo<ItemStack> ingrInfo =
            new IngredientInfo<>(
                VanillaTypes.ITEM_STACK,
                im.getAllIngredients(VanillaTypes.ITEM_STACK),
                im.getIngredientHelper(VanillaTypes.ITEM_STACK),
                im.getIngredientRenderer(VanillaTypes.ITEM_STACK)
            );

        RegisteredIngredients ri = new RegisteredIngredients(List.of(ingrInfo));
        Optional<ITypedIngredient<ItemStack>> typedIngredient = TypedIngredient.createTyped(ri, VanillaTypes.ITEM_STACK, newStack);
        if (typedIngredient.isEmpty()) {
            MutableComponent cmp = new TextComponent("Couldn't import bookmark from clipboard. Blank JEI Ingredient.");
            StatusOverlay.renderText(cmp);
            return;
        }

        MutableComponent cmp = new TextComponent("Imported ").append(newStack.getDisplayName()).append(" bookmark from clipboard.");
        StatusOverlay.renderText(cmp);

        bookmarkList.add(typedIngredient.get());
    }
}
