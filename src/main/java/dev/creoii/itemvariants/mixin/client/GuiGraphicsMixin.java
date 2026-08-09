package dev.creoii.itemvariants.mixin.client;

import dev.creoii.itemvariants.util.VariantItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsMixin {
    @ModifyConstant(method = "lambda$setTooltipForNextFrame$0", constant = @Constant(intValue = 0))
    private static int gbw$modifyTooltipImageForVariant00(int constant) {
        if (Minecraft.getInstance().gui.screen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }

    @ModifyConstant(method = "lambda$setTooltipForNextFrame$0", constant = @Constant(intValue = 1))
    private static int gbw$modifyTooltipImageForVariant10(int constant) {
        if (Minecraft.getInstance().gui.screen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }

    @ModifyConstant(method = "lambda$setTooltipForNextFrame$1", constant = @Constant(intValue = 0))
    private static int gbw$modifyTooltipImageForVariant01(int constant) {
        if (Minecraft.getInstance().gui.screen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }

    @ModifyConstant(method = "lambda$setTooltipForNextFrame$1", constant = @Constant(intValue = 1))
    private static int gbw$modifyTooltipImageForVariant11(int constant) {
        if (Minecraft.getInstance().gui.screen() instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }
}
