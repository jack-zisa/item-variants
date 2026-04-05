package dev.creoii.itemvariants;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class Variant {
    private final Set<Item> items;
    private final Set<Tag.Named<Item>> itemTags;
    private String translationKey;

    public Variant() {
        items = new HashSet<>();
        itemTags = new HashSet<>();
    }

    public Variant build(ResourceLocation id) {
        this.translationKey = "variant." + id.getPath();
        return this;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Set<Tag.Named<Item>> getItemTags() {
        return itemTags;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean isStackInTags(ItemStack stack) {
        for (Tag.Named<Item> tagKey : itemTags) {
            if (stack.getItem().is(tagKey))
                return true;
        }
        return false;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void addItemTag(Tag.Named<Item> itemTag) {
        itemTags.add(itemTag);
    }

    public void copyTo(Variant other) {
        itemTags.forEach(other::addItemTag);
        items.forEach(other::addItem);
    }

    public static class Serializer implements JsonDeserializer<Variant>, JsonSerializer<Variant> {
        @Override
        public Variant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                Variant variant = new Variant();

                JsonArray values = GsonHelper.getAsJsonArray(object, "values");
                values.forEach(value -> {
                    if (value.isJsonPrimitive()) {
                        String pValue = value.getAsString();
                        if (pValue.startsWith("#")) {
                            Tag.Named<Item> tagKey = ItemTags.HELPER.bind(pValue.substring(1));
                            variant.addItemTag(tagKey);
                        } else {
                            ResourceLocation id = ResourceLocation.tryParse(pValue);
                            if (Registry.ITEM.containsKey(id)) {
                                variant.addItem(Registry.ITEM.get(id));
                            } else ItemVariants.LOGGER.warn("Found unknown item id '{}' in a variant.", id);
                        }
                    }
                });
                return variant;
            }
            throw new JsonParseException("Variant definition is not a JsonObject.");
        }

        @Override
        public JsonElement serialize(Variant src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            JsonArray array = new JsonArray();

            src.getItemTags().forEach(tagKey -> array.add("#" + tagKey.getName()));
            src.getItems().forEach(item -> array.add(Registry.ITEM.getKey(item).toString()));

            obj.add("values", array);
            return obj;
        }
    }
}