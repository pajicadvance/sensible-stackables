package me.pajic.sensible_stackables;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(value = Main.MOD_ID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerMain {

    public ServerMain() {}

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        Main.debugLog("Applying stack sizes");
        Main.patchItems(event.getServer().overworld());
    }
}
