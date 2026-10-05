package me.pajic.sensible_stackables.platform.fabric;

//? fabric {

//~ if <26.1 'ServerLevelEvents' -> 'ServerWorldEvents' {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.handler.StackSizeSyncPayload;
import me.pajic.sensible_stackables.platform.MultiLoaderUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		SensibleStackables.onInitialize();
        //~ if <26.1 'clientboundPlay' -> 'playS2C'
        PayloadTypeRegistry.clientboundPlay().register(StackSizeSyncPayload.TYPE, StackSizeSyncPayload.CODEC);
        ServerLevelEvents.LOAD.register((s, level) -> ItemSuggestions.update(level));
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            if (!client) StackSizeOverrides.computeAndSet(registries);
        });
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, j) ->
                MultiLoaderUtil.INSTANCE.s2c(player, new StackSizeSyncPayload(StackSizeOverrides.snapshot()))
        );
	}
}
//~}
//?}
