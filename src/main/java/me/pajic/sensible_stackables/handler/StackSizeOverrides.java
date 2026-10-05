package me.pajic.sensible_stackables.handler;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.extension.ItemExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public final class StackSizeOverrides {

    private static volatile Reference2IntMap<Item> table = empty();

    public static void computeAndSet(HolderLookup.Provider provider) {
        var lookup = provider.lookupOrThrow(Registries.ITEM);
        var cfg = SensibleStackables.CONFIG;
        boolean uncapped = cfg.uncapStackSize.get();
        Map<Item, Integer> common = new HashMap<>(), tagged = new HashMap<>(), explicit = new HashMap<>();

        int commonSize = cfg.commonStackSize.get();
        if (commonSize != 64 && (uncapped || commonSize <= 99)) {
            lookup.listElements().forEach(h -> {
                Item item = h.value();
                int raw = item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
                if (!((ItemExtension) item).sensible_stackables$isDamageable() && raw == 64) {
                    common.put(item, commonSize);
                }
            });
        }
        cfg.items.get().forEach((key, size) -> {
            int s = uncapped ? size : Math.min(size, 99);
            if (key.startsWith("#")) {
                lookup.get(TagKey.create(Registries.ITEM, Identifier.parse(key.substring(1))))
                      .ifPresent(set -> set.forEach(h -> tagged.put(h.value(), s)));
            } else {
                lookup.get(ResourceKey.create(Registries.ITEM, Identifier.parse(key)))
                      .ifPresent(h -> explicit.put(h.value(), s));
            }
        });

        clear();
        table.putAll(common);
        table.putAll(tagged);
        table.putAll(explicit);
    }

    private static Reference2IntMap<Item> empty() {
        var m = new Reference2IntOpenHashMap<Item>();
        m.defaultReturnValue(-1);
        return m;
    }

    public static int get(Item item) {
        return table.getInt(item);
    }

    public static Map<Item, Integer> snapshot() {
        return table;
    }

    public static void set(Map<Item, Integer> m) {
        clear();
        table.putAll(m);
    }

    public static void clear() {
        table = empty();
    }
}
