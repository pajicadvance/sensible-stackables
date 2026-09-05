package me.pajic.sensible_stackables.defaulted;

import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.extension.ItemExtension;
import net.atlas.defaulted.Defaulted;
import net.atlas.defaulted.component.ItemPatches;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MaxStackSizePatchEvent {

	public static void register() {
		Defaulted.builtinPatchCreator((registry, patchApplier) -> {
			HolderLookup<Item> lookup = registry.lookupOrThrow(Registries.ITEM);
            Set<ResourceKey<Item>> itemsWithDefaultStackSize = new HashSet<>();
            lookup.listElementIds().forEach(key -> {
                Item item = lookup.getOrThrow(key).value();
                if (!((ItemExtension) item).sensible_stackables$isDamageable() && item.getDefaultMaxStackSize() == 64) {
                    itemsWithDefaultStackSize.add(key);
                }
            });
			Map<Integer, PatchData> patches = new HashMap<>();
			int commonSize = SensibleStackables.CONFIG.commonStackSize.get();
			if (commonSize != 64 && (SensibleStackables.CONFIG.uncapStackSize.get() || commonSize <= 99)) {
				PatchData data = patches.getOrDefault(commonSize, new PatchData(new ArrayList<>(), new ArrayList<>(), 10));
				lookup.listElementIds().forEach(key -> {
					if (itemsWithDefaultStackSize.contains(key)) data.items.add(lookup.getOrThrow(key));
				});
				if (!patches.containsKey(commonSize)) patches.put(commonSize, data);
			}
			SensibleStackables.CONFIG.items.get().forEach((s, i) -> {
				PatchData data = patches.getOrDefault(i, new PatchData(new ArrayList<>(), new ArrayList<>(), 1000));
				if (s.startsWith("#")) lookup.get(TagKey.create(Registries.ITEM, Identifier.parse(s.substring(1)))).ifPresent(data.tags::add);
				else lookup.get(ResourceKey.create(Registries.ITEM, Identifier.parse(s))).ifPresent(data.items::add);
				if (!patches.containsKey(i)) patches.put(i, data);
			});
			patches.forEach((i, data) -> {
				List<HolderSet<Item>> elements = new ArrayList<>();
				elements.add(HolderSet.direct(data.items));
				elements.addAll(data.tags);
				patchApplier.put(
						SensibleStackables.id(String.valueOf(i)),
						new ItemPatches(
								elements, List.of(),
								DataComponentPatch.builder().set(DataComponents.MAX_STACK_SIZE, i).build(),
								data.priority
						)
				);
				log(i, data);
			});
		});
	}

	private static void log(int i, PatchData data) {
		SensibleStackables.debugLog("-----------------------------------------------------------");
		SensibleStackables.debugLog("Stack size {}", i);
		SensibleStackables.debugLog("Priority {}", data.priority);
		SensibleStackables.debugLog("Items:");
		data.items.forEach(itemHolder -> SensibleStackables.debugLog("- {}", itemHolder.getRegisteredName()));
		SensibleStackables.debugLog("Tags:");
		data.tags.forEach(tag -> SensibleStackables.debugLog("- {}", tag.key().location().toString()));
	}

	private record PatchData(List<Holder<Item>> items, List<HolderSet.Named<Item>> tags, int priority) {}
}
