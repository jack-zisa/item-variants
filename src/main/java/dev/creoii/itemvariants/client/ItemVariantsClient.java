package dev.creoii.itemvariants.client;

import dev.creoii.itemvariants.util.VariantItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;

public class ItemVariantsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            Minecraft.getInstance().getLanguageManager().onResourceManagerReload(client.getResourceManager());
        });

        ItemTooltipCallback.EVENT.register((stack, context, tooltipType, list) -> {
            if (stack.getItem() instanceof VariantItem variantItem && !ItemStack.matches(stack, Raid.getOminousBannerInstance(context.registries().lookupOrThrow(Registries.BANNER_PATTERN)))) {
                if (!variantItem.gbw$getVariants().isEmpty()) {
                    list.add(1, VariantItem.getVariantTooltip(variantItem));
                }
            }
        });
    }
}
