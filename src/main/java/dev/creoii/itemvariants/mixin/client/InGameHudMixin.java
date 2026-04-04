package dev.creoii.itemvariants.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.itemvariants.util.VariantItem;
import net.fabricmc.loader.api.FabricLoader;
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

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Shadow private ItemStack lastToolHighlight;
    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "renderSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"))
    private void gbw$renderHeldItemVariants(GuiGraphics instance, Font font, Component component, int x, int y, int width, int color, Operation<Void> original) {
        if (minecraft.player != null && minecraft.player.getArmorValue() > 0) {
            y -= 10;
        }
        if (lastToolHighlight.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
            instance.drawCenteredString(font, VariantItem.getVariantTooltip(variantItem), x + (font.width(component.getString()) / 2), y + 10, color);
        } else if (FabricLoader.getInstance().isModLoaded("great_big_world")) {
             if (lastToolHighlight.getItem() instanceof SpawnEggItem spawnEggItem && instance.minecraft.level != null) {
                MutableComponent mutableText = MutableComponent.create(spawnEggItem.getType(lastToolHighlight).getDescription().getContents()).withStyle(ChatFormatting.GRAY);
                instance.drawCenteredString(font, mutableText, x + (font.width(component.getString()) / 2), y + 10, color);
            } else if (lastToolHighlight.is(ItemTags.DECORATED_POT_SHERDS)) {
                Identifier id = BuiltInRegistries.ITEM.getKey(lastToolHighlight.getItem());
                instance.drawCenteredString(font, Component.translatable("variant.item.sherd." + id.getPath().replace("_pottery_sherd", "")).withStyle(ChatFormatting.GRAY), x + (font.width(component.getString()) / 2), y + 10, color);
            }
        }

        instance.drawStringWithBackdrop(font, component, x, y, width, color);
    }
}
