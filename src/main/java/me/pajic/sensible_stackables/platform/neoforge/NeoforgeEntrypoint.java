package me.pajic.sensible_stackables.platform.neoforge;

//? neoforge {

/*import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(SensibleStackables.MOD_ID)
@EventBusSubscriber(modid = SensibleStackables.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
        SensibleStackables.onInitialize();
	}

    @SubscribeEvent
    private static void onLevelLoad(LevelEvent.Load event) {
        ItemSuggestions.update(event.getLevel());
    }
}
*///?}
