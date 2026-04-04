package dev.creoii.itemvariants;

import dev.creoii.itemvariants.util.VariantItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemVariants implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(ItemVariants.class);

    @Override
    public void onInitialize() {
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof VariantItem variantItem) {
                for (Variant variant : VariantLoader.VARIANTS.values()) {
                    if (variant.getItems().contains(variantItem) || variant.isStackInTags(item.getDefaultInstance()))
                        variantItem.gbw$addVariant(variant);
                }
            }
        });

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(Identifier.fromNamespaceAndPath("great_big_world", "variant"), new VariantLoader());
    }
}
