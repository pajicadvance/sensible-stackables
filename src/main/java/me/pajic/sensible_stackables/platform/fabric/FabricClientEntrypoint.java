package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

//~ if <26.1 'ClientLevelEvents' -> 'ClientWorldEvents' {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.sensible_stackables.SensibleStackablesClient;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.handler.StackSizeSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
        SensibleStackablesClient.onInitialize();
        ClientPlayNetworking.registerGlobalReceiver(
                StackSizeSyncPayload.TYPE,
                (payload, c) -> StackSizeOverrides.set(payload.sizes())
        );
        //~ if <26.1 'AFTER_CLIENT_LEVEL_CHANGE' -> 'AFTER_CLIENT_WORLD_CHANGE'
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((c, level) -> ItemSuggestions.update(level));
        ClientPlayConnectionEvents.DISCONNECT.register((l, c) -> StackSizeOverrides.clear());
	}
}
//~}
//?}
