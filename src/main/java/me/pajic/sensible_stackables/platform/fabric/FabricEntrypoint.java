package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

import me.pajic.sensible_stackables.SensibleStackables;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SensibleStackables.onInitialize();
	}
}
//?}
