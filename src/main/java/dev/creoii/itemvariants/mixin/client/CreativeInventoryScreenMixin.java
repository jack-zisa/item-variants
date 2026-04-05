package dev.creoii.itemvariants.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Redirect(method = "renderTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    private <E> void gbw$fixTooltipOrder(List<Component> instance, int i, E e) {
        instance.add(Math.min(i + 1, instance.size()), (Component) e);
    }
}
