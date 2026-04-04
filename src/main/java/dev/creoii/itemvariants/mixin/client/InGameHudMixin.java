package dev.creoii.itemvariants.mixin.client;

import dev.creoii.itemvariants.util.VariantItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Shadow private ItemStack lastToolHighlight;
    @Shadow @Final private Minecraft minecraft;

    @Redirect(method = "renderSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"))
    private void gbw$renderHeldItemVariants(GuiGraphics instance, Font textRenderer, Component text, int x, int y, int width, int color) {
        if (minecraft.player != null && minecraft.player.getArmorValue() > 0) {
            y -= 10;
        }
        if (lastToolHighlight.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
            instance.drawCenteredString(textRenderer, VariantItem.getVariantTooltip(variantItem), x + (textRenderer.width(text.getString()) / 2), y + 10, color);
        } else if (lastToolHighlight.getItem() instanceof SpawnEggItem spawnEggItem && instance.minecraft.level != null) {
            MutableComponent mutableText = MutableComponent.create(spawnEggItem.getType(lastToolHighlight).getDescription().getContents()).withStyle(ChatFormatting.GRAY);
            instance.drawCenteredString(textRenderer, mutableText, x + (textRenderer.width(text.getString()) / 2), y + 10, color);
        } else if (lastToolHighlight.is(ItemTags.DECORATED_POT_SHERDS)) {
            Identifier id = BuiltInRegistries.ITEM.getKey(lastToolHighlight.getItem());
            instance.drawCenteredString(textRenderer, Component.translatable("variant.item.sherd." + id.getPath().replace("_pottery_sherd", "")).withStyle(ChatFormatting.GRAY), x + (textRenderer.width(text.getString()) / 2), y + 10, color);
        }

        instance.drawStringWithBackdrop(textRenderer, text, x, y, width, color);
    }
}
