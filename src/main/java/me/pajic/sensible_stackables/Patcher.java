package me.pajic.sensible_stackables;

import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;

public class Patcher {

	public static void patchItems(HolderLookup.Provider provider) {
		SensibleStackables.debugLog("Applying stack sizes");
		HolderLookup<Item> registry = provider.lookupOrThrow(Registries.ITEM);
		ModConfig.CONFIG.items().forEach((s, i) -> {
			if (s.startsWith("#")) {
				Identifier rl = Identifier.tryParse(s.substring(1));
				if (rl != null) registry.get(TagKey.create(Registries.ITEM, rl)).ifPresent(item ->
						item.forEach(itemHolder -> patchItem(itemHolder.value(), i))
				);
				else SensibleStackables.LOGGER.error("Item tag {} not found", s);
			} else {
				Identifier rl = Identifier.tryParse(s);
				if (rl != null) registry.get(ResourceKey.create(Registries.ITEM, rl)).ifPresent(item -> patchItem(item.value(), i));
				else SensibleStackables.LOGGER.error("Item {} not found", s);
			}
		});
	}

	@SuppressWarnings("deprecation")
	private static void patchItem(Item item, int i) {
		if (!item.components().has(DataComponents.MAX_DAMAGE) && i != item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 1)) {
			int stackSize = Mth.clamp(i, 1, ModConfig.CONFIG.uncapStackSize() ? Integer.MAX_VALUE : 64);
			item.builtInRegistryHolder().bindComponents(PatchedDataComponentMap.fromPatch(
					item.components(),
					DataComponentPatch.builder().set(DataComponents.MAX_STACK_SIZE, stackSize).build()
			));
			SensibleStackables.debugLog("Set stack size for {} to {}", item.getDescriptionId(), stackSize);
		}
	}
}
