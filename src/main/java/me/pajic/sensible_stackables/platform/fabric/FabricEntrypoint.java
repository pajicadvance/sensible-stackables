package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

import me.pajic.sensible_stackables.Patcher;
import me.pajic.sensible_stackables.SensibleStackables;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SensibleStackables.onInitialize();
		initPatchEvent();
	}

	private void initPatchEvent() {
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> Patcher.patchItems(registries));
	}
}
//?}
