package dev.creoii.itemvariants.mixin.client;

import dev.creoii.itemvariants.util.LocaleAwareLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.locale.Language;
import net.minecraft.server.packs.resources.Resource;

@Mixin(ClientLanguage.class)
public class TranslationStorageMixin {
    @Inject(method = "appendFrom(Ljava/lang/String;Ljava/util/List;Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/Language;loadFromJson(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V"))
    private static void gbw$applyLocaleAwareLanguage(String langCode, List<Resource> resourceRefs, Map<String, String> translations, CallbackInfo ci) {
        if (Language.getInstance() != null)
            ((LocaleAwareLanguage) Language.getInstance()).gbw$setLangCode(langCode);
    }
}