package me.pajic.sensible_stackables.platform.neoforge;

//? neoforge {

/*import me.pajic.sensible_stackables.Patcher;
import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.DefaultDataComponentsBoundEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod(SensibleStackables.MOD_ID)
@EventBusSubscriber(modid = SensibleStackables.MOD_ID)
public class NeoforgeEntrypoint {

	private static HolderLookup.Provider lookup = null;

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
		SensibleStackables.onInitialize();
	}

	@SubscribeEvent
	private static void onTagsUpdated(TagsUpdatedEvent event) {
		lookup = event.getLookupProvider();
	}

	@SubscribeEvent
	private static void onComponentsLoaded(DefaultDataComponentsBoundEvent event) {
		if (lookup != null) Patcher.patchItems(lookup);
	}
}
*///?}
