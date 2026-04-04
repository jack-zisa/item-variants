package dev.creoii.itemvariants.mixin.client;

import dev.creoii.itemvariants.util.VariantItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @ModifyConstant(method = "method_51442", constant = @Constant(intValue = 0))
    private static int gbw$modifyTooltipImageForVariant0(int constant) {
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }

    @ModifyConstant(method = "method_51442", constant = @Constant(intValue = 1))
    private static int gbw$modifyTooltipImageForVariant1(int constant) {
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> abstractContainerScreen) {
            ItemStack stack = abstractContainerScreen.hoveredSlot.getItem();
            if (stack.getItem() instanceof VariantItem variantItem && !variantItem.gbw$getVariants().isEmpty()) {
                return constant + 1;
            }
        }
        return constant;
    }
}
