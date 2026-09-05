package me.pajic.sensible_stackables.config;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class ItemSuggestions {

	private static final List<String> VALUES = new ArrayList<>();

	public static void update(LevelAccessor level) {
        //~ if <26.1 'lookupOrThrow' -> 'registryOrThrow'
        Registry<Item> registry = level.registryAccess().lookupOrThrow(Registries.ITEM);
		VALUES.clear();
		VALUES.addAll(registry.keySet().stream().map(Identifier::toString).toList());
        //~ if <26.1 'listTagIds' -> 'getTagNames'
        //~ if <26.1 'tag.location()' -> 'tag.location()'
		registry.listTagIds().forEach(tag -> VALUES.add("#" + tag.location()));
	}

	public static List<String> get() {
		return VALUES;
	}
}
