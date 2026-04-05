package dev.creoii.itemvariants.util;

import dev.creoii.itemvariants.Variant;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Iterator;
import java.util.Set;

public interface VariantItem {
    Set<Variant> gbw$getVariants();

    default void gbw$addVariant(Variant variant) {
        gbw$getVariants().add(variant);
    }

    static Component getVariantTooltip(VariantItem variantItem) {
        MutableComponent text = new TextComponent("");
        Iterator<Variant> iterator = variantItem.gbw$getVariants().iterator();
        while (iterator.hasNext()) {
            Variant variant = iterator.next();
            text.append(new TranslatableComponent(variant.getTranslationKey()).withStyle(ChatFormatting.GRAY));
            if (iterator.hasNext())
                text.append(new TextComponent(", ").withStyle(ChatFormatting.GRAY));
        }
        return text;
    }
}
