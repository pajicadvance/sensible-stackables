package me.pajic.sensible_stackables.defaulted;

import me.pajic.sensible_stackables.SensibleStackables;
import net.atlas.defaulted.Defaulted;
import net.atlas.defaulted.component.ItemPatches;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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

	public static Set<ResourceKey<Item>> itemsWithDefaultStackSize = new HashSet<>();

	public static void register() {
		Defaulted.builtinPatchCreator(patchApplier -> {
			Map<Integer, PatchData> patches = new HashMap<>();
			int commonSize = SensibleStackables.CONFIG.commonStackSize.get();
			if (commonSize != 64 && (SensibleStackables.CONFIG.uncapStackSize.get() || commonSize <= 99)) {
				PatchData data = patches.getOrDefault(commonSize, new PatchData(new ArrayList<>(), new ArrayList<>(), 10));
				BuiltInRegistries.ITEM.listElementIds().forEach(key -> {
					if (itemsWithDefaultStackSize.contains(key)) data.items.add(BuiltInRegistries.ITEM.getOrThrow(key));
				});
				if (!patches.containsKey(commonSize)) patches.put(commonSize, data);
			}
			SensibleStackables.CONFIG.items.get().forEach((s, i) -> {
				PatchData data = patches.getOrDefault(i, new PatchData(new ArrayList<>(), new ArrayList<>(), 1000));
				if (s.startsWith("#")) data.tags.add(TagKey.create(Registries.ITEM, Identifier.parse(s.substring(1))));
				else BuiltInRegistries.ITEM.get(Identifier.parse(s)).ifPresent(data.items::add);
				if (!patches.containsKey(i)) patches.put(i, data);
			});
			patches.forEach((i, data) -> {
				patchApplier.put(
						SensibleStackables.id(String.valueOf(i)),
						new ItemPatches(
								HolderSet.direct(data.items), data.tags, List.of(),
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
		data.tags.forEach(tagKey -> SensibleStackables.debugLog("- {}", tagKey.location().toString()));
	}

	private record PatchData(List<Holder<Item>> items, List<TagKey<Item>> tags, int priority) {}
}
