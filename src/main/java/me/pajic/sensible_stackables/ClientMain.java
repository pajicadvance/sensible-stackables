package me.pajic.sensible_stackables;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;

public class ClientMain implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Main.S2CSyncConfigPayload.TYPE, (payload, context) -> {
            if (!context.client().hasSingleplayerServer()) {
                Main.debugLog("Applying stack sizes from server configuration");
                ModConfig.CONFIG = new ModConfig.Config(payload.items(), payload.splashPotionCooldown(), payload.uncapStackSize());
                Main.patchItems(context.client().level.registryAccess());
            }
        });
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            Main.debugLog("Applying stack sizes from local configuration");
            ModConfig.loadConfig();
            Main.patchItems(registries);
        });
    }
}
