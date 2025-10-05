package me.pajic.sensible_stackables;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class ServerMain implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerWorldEvents.LOAD.register((server, level) -> {
            Main.debugLog("Applying stack sizes");
            Main.patchItems(level);
        });
    }
}
