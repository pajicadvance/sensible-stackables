package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.sensible_stackables.SensibleStackablesClient;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SensibleStackablesClient.onInitialize();
	}
}
//?}
