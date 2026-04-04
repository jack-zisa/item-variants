package dev.creoii.itemvariants.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.creoii.itemvariants.Variant;
import dev.creoii.itemvariants.util.VariantItem;
import dev.creoii.itemvariants.VariantLoader;
import dev.creoii.itemvariants.util.LocaleAwareLanguage;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.BiConsumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@Mixin(Language.class)
public class LanguageMixin implements LocaleAwareLanguage {
    @Shadow private static volatile Language instance;
    @Unique
    private String gbw$langCode;

    @WrapWithCondition(method = "loadFromJson(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V", at = @At(value = "INVOKE", target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V"))
    private static boolean gbw$applyTranslationLoadEvent(BiConsumer<String, String> entryConsumer, Object key, Object value) {
        if (instance == null)
            return true;

        String langCode = ((LocaleAwareLanguage) Language.getInstance()).gbw$getLangCode();
        if (langCode == null || !langCode.equals("en_us"))
            return true;

        String translationKey = (String) key;
        String translated = (String) value;

        Item item = BuiltInRegistries.ITEM.get(toId(translationKey));
        return renameItemForVariants(item, entryConsumer, translationKey, translated);
    }

    @Override
    public String gbw$getLangCode() {
        return gbw$langCode;
    }

    public void gbw$setLangCode(String langCode) {
        gbw$langCode = langCode;
    }

    @Unique
    private static ResourceLocation toId(String translationKey) {
        translationKey = translationKey.toLowerCase();

        int dot1 = translationKey.indexOf('.') + 1;
        int dot2 = translationKey.indexOf('.', dot1);
        int dot3 = translationKey.indexOf('.', dot2 + 1);

        if (dot2 < 0)
            return ResourceLocation.tryParse("air");

        String path;
        if (dot3 <= 0) path = translationKey.substring(dot2 + 1);
        else path = translationKey.substring(dot2 + 1, dot3);

        return new ResourceLocation(translationKey.substring(dot1, dot2), path);
    }

    @Unique
    private static boolean renameItemForVariants(Item item, BiConsumer<String, String> consumer, String translationKey, String translated) {
        if (item == Items.AIR)
            return true;
        if (item instanceof VariantItem variantItem) {
            for (Variant variant : VariantLoader.VARIANTS.values()) {
                if (variant.getItems().contains(item) || variant.isStackInTags(item.getDefaultInstance())) {
                    variantItem.gbw$addVariant(variant);
                }
            }

            if (!variantItem.gbw$getVariants().isEmpty()) {
                String variantKey = translationKey.endsWith(".variant") ? translationKey : translationKey + ".variant";
                String translated1 = Component.translatable(variantKey).getString();
                if (!translated1.equals(translated)) {
                    consumer.accept(translationKey, translated1);
                    return false;
                }
            }
        }
        return true;
    }
}