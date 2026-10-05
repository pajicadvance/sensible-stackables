package me.pajic.sensible_stackables.platform.neoforge;

//? neoforge {

/*import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.handler.StackSizeSyncPayload;
import me.pajic.sensible_stackables.platform.MultiLoaderUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(SensibleStackables.MOD_ID)
@EventBusSubscriber(modid = SensibleStackables.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
        SensibleStackables.onInitialize();
	}

    @SubscribeEvent
    private static void initNetworking(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(StackSizeSyncPayload.TYPE, StackSizeSyncPayload.CODEC
                /^? <26.1 {^//^, (payload, c) -> StackSizeOverrides.set(payload.sizes())^//^?}^/
        );
    }

    @SubscribeEvent
    private static void onLevelLoad(LevelEvent.Load event) {
        ItemSuggestions.update(event.getLevel());
    }

    @SubscribeEvent
    private static void onSyncDatapacks(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) StackSizeOverrides.computeAndSet(event.getPlayer().registryAccess());
        event.getPlayerList().getPlayers().forEach(player ->
                MultiLoaderUtil.INSTANCE.s2c(player, new StackSizeSyncPayload(StackSizeOverrides.snapshot()))
        );
    }
}
*///?}
