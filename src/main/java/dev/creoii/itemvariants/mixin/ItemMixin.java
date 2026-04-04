package dev.creoii.itemvariants.mixin;

import dev.creoii.itemvariants.Variant;
import dev.creoii.itemvariants.util.VariantItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.item.Item;

@Mixin(Item.class)
public class ItemMixin implements VariantItem {
    @Unique
    private final Set<Variant> gbw$variants = new HashSet<>();

    @Override
    public Set<Variant> gbw$getVariants() {
        return gbw$variants;
    }
}
