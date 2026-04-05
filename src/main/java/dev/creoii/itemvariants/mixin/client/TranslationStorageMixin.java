package dev.creoii.itemvariants.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
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
    @Inject(method = "appendFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/locale/Language;loadFromJson(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V"))
    private static void gbw$applyLocaleAwareLanguage(List<Resource> list, Map<String, String> map, CallbackInfo ci, @Local Resource resource) {
        if (Language.getInstance() != null) {
            String path = resource.getLocation().getPath();
            String langCode = path.substring(path.lastIndexOf('/') + 1).replace(".json", "");
            ((LocaleAwareLanguage) Language.getInstance()).gbw$setLangCode(langCode);
        }
    }
}