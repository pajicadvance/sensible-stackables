package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

//~ if <26.1 'ServerLevelEvents' -> 'ServerWorldEvents' {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SensibleStackables.onInitialize();
        ServerLevelEvents.LOAD.register((server, level) -> ItemSuggestions.update(level));
	}
}
//~}
//?}
