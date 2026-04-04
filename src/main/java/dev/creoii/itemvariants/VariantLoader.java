package dev.creoii.itemvariants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariantLoader extends SimplePreparableReloadListener<Map<String, Variant>> {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Variant.class, new Variant.Serializer()).create();
    public static final Map<String, Variant> VARIANTS = new HashMap<>();

    @Override
    protected Map<String, Variant> prepare(ResourceManager manager, ProfilerFiller profilerFiller) {
        Map<String, Variant> variants = new HashMap<>();

        Map<Identifier, List<Resource>> resourceMap = manager.listResourceStacks("variants", path -> path.getPath().endsWith(".json"));
        for (Map.Entry<Identifier, List<Resource>> entry : resourceMap.entrySet()) {
            Identifier identifier = entry.getKey();
            for (Resource resource : entry.getValue()) {
                try (InputStream stream = resource.open()) {
                    String result = IOUtils.toString(stream, StandardCharsets.UTF_8);
                    Identifier identifier1 = Identifier.fromNamespaceAndPath(identifier.getNamespace(), identifier.getPath().replace("variants/", "").replace(".json", ""));
                    Variant variant = GSON.fromJson(result, Variant.class).build(identifier1);

                    if (variant.getItems().isEmpty() && variant.getItemTags().isEmpty()) {
                        ItemVariants.LOGGER.warn("Found empty variant definition: '{}'", identifier);
                        continue;
                    }

                    if (variants.containsKey(identifier1.getPath())) {
                        variant.copyTo(variants.get(identifier1.getPath()));
                    } else variants.put(identifier1.getPath(), variant);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return variants;
    }

    @Override
    protected void apply(Map<String, Variant> map, ResourceManager manager, ProfilerFiller profilerFiller) {
        VARIANTS.clear();
        VARIANTS.putAll(map);
        int entries = VARIANTS.entrySet().stream().map(entry -> entry.getValue().getItems().size() + entry.getValue().getItemTags().size()).reduce(0, Integer::sum);
        ItemVariants.LOGGER.info("Loaded {} variants with {} total entries", VARIANTS.size(), entries);
    }
}
