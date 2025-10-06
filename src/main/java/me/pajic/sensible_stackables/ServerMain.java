package me.pajic.sensible_stackables;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

public class ServerMain implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            Main.debugLog("Applying stack sizes");
            ModConfig.loadConfig();
            Main.patchItems(registries);
        });
    }
}
