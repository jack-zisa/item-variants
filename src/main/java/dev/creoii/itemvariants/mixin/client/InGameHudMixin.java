package dev.creoii.itemvariants.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.itemvariants.util.VariantItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

    @WrapOperation(method = "renderSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;FFI)I"))
    private int gbw$renderHeldItemVariants(Font instance, PoseStack poseStack, Component component, float x, float y, int color, Operation<Integer> original, @Local(ordinal = 0) int i) {
        if (minecraft.player != null && minecraft.player.getArmorValue() > 0) {
            y -= 10;
        }

        float centerX = x + (i / 2f);

        if (lastToolHighlight.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
            Component variantText = VariantItem.getVariantTooltip(variantItem);
            instance.drawShadow(poseStack, variantText, centerX - (instance.width(variantText) / 2f), y + 10, color);
        } else if (FabricLoader.getInstance().isModLoaded("great_big_world")) {
             if (lastToolHighlight.getItem() instanceof SpawnEggItem spawnEggItem) {
                 MutableComponent mutableText = MutableComponent.create(spawnEggItem.getType(lastToolHighlight.getTag()).getDescription().getContents()).withStyle(ChatFormatting.GRAY);
                 instance.drawShadow(poseStack, mutableText, centerX - (instance.width(mutableText) / 2f), y + 10, color);
             }
        }

        return instance.drawShadow(poseStack, component, x, y, color);
    }
}
