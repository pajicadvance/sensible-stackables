package me.pajic.sensible_stackables.mixson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.MinecartItem;
import net.ramixin.mixson.util.Index;

public class DynamicTagEvent {

	public static void register() {
		MixsonHelper.registerSingleJson(
				"Generate All Block Items tag",
				new Index("sensible_stackables:tags/item/all_block_items"),
				context -> {
					JsonArray values = context.getFile().getAsJsonObject().getAsJsonArray("values");
                    //~ if >=26.1 'registryKeySet' -> 'listElementIds'
					BuiltInRegistries.BLOCK.listElementIds().forEach(key -> {
						JsonObject o = new JsonObject();
						o.addProperty("required", false);
                        //~ if >=26.1 'location' -> 'identifier'
						o.addProperty("id", key.identifier().toString());
						values.add(o);
					});
				}
		);
		MixsonHelper.registerSingleJson(
				"Generate Minecarts tag",
				new Index("sensible_stackables:tags/item/minecarts"),
				context -> {
					JsonArray values = context.getFile().getAsJsonObject().getAsJsonArray("values");
                    //~ if >=26.1 'registryKeySet' -> 'listElementIds'
					BuiltInRegistries.ITEM.listElementIds().forEach(key -> {
                        //~ if >=26.1 'getOrThrow' -> 'getValueOrThrow'
						if (BuiltInRegistries.ITEM.getValueOrThrow(key) instanceof MinecartItem) {
							JsonObject o = new JsonObject();
							o.addProperty("required", false);
                            //~ if >=26.1 'location' -> 'identifier'
							o.addProperty("id", key.identifier().toString());
							values.add(o);
						}
					});
				}
		);
	}
}
