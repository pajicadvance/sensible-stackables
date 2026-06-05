package me.pajic.sensible_stackables.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ItemSuggestions {

	private static final List<String> VALUES = new ArrayList<>();

	public static void update() {
		VALUES.clear();
		VALUES.addAll(BuiltInRegistries.ITEM.keySet().stream().map(Identifier::toString).toList());
		BuiltInRegistries.ITEM.listTagIds().forEach(tag -> VALUES.add("#" + tag.location()));
	}

	public static List<String> get() {
		return VALUES;
	}
}
