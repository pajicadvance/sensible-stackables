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

        int commonSize = uncapped ? cfg.commonStackSize.get() : Math.min(cfg.commonStackSize.get(), 99);
        if (commonSize != 64) lookup.listElements().forEach(h -> {
            Item item = h.value();
            if (!isDamageable(item) && getVanillaMaxStackSize(item) == 64) common.put(item, commonSize);
        });
        cfg.items.get().forEach((key, size) -> {
            int s = uncapped ? size : Math.min(size, 99);
            boolean isTag = key.startsWith("#");
            Identifier id = Identifier.tryParse(isTag ? key.substring(1) : key);
            if (id == null) {
                SensibleStackables.LOGGER.warn("Skipping invalid {} id in config: {}", isTag ? "tag" : "item", key);
                return;
            }
            if (isTag) lookup.get(TagKey.create(Registries.ITEM, id)).ifPresent(set -> set.forEach(h -> {
                if (!isDamageable(h.value())) tagged.put(h.value(), s);
            }));
            else lookup.get(ResourceKey.create(Registries.ITEM, id)).ifPresent(h -> {
                if (isDamageable(h.value())) {
                    SensibleStackables.LOGGER.warn("Skipping damageable item in config, it can't be stacked: {}", key);
                } else explicit.put(h.value(), s);
            });
        });

        var next = empty();
        next.putAll(common);
        next.putAll(tagged);
        next.putAll(explicit);
        table = next;
    }

    private static Reference2IntMap<Item> empty() {
        var m = new Reference2IntOpenHashMap<Item>();
        m.defaultReturnValue(-1);
        return m;
    }

    private static boolean isDamageable(Item item) {
        return ((ItemExtension) item).sensible_stackables$isDamageable();
    }

    private static int getVanillaMaxStackSize(Item item) {
        return item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 1);
    }

    public static int get(Item item) {
        return table.getInt(item);
    }

    public static int lenientMaxStackSize(Item item, int maxStackSize) {
        return maxStackSize == get(item) ? Math.max(maxStackSize, getVanillaMaxStackSize(item)) : maxStackSize;
    }

    public static Map<Item, Integer> snapshot() {
        return table;
    }

    public static void set(Map<Item, Integer> m) {
        var next = empty();
        next.putAll(m);
        table = next;
    }

    public static void clear() {
        table = empty();
    }
}
