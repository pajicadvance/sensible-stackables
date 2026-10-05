package me.pajic.sensible_stackables.platform.neoforge;

//? neoforge {

/*import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.SensibleStackablesClient;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

//? >=26.1 {
import me.pajic.sensible_stackables.handler.StackSizeSyncPayload;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
//?}

@EventBusSubscriber(modid = SensibleStackables.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLCommonSetupEvent event) {
        SensibleStackablesClient.onInitialize();
	}

    //? >=26.1 {
    @SubscribeEvent
    private static void initNetworking(RegisterClientPayloadHandlersEvent event) {
        event.register(StackSizeSyncPayload.TYPE, (payload, c) ->
                StackSizeOverrides.set(payload.sizes())
        );
    }
    //?}

    @SubscribeEvent
    private static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        StackSizeOverrides.clear();
    }
}
*///?}
