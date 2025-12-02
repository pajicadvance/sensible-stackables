package me.pajic.sensible_stackables.platform.neoforge;

//? neoforge {

/*import me.pajic.sensible_stackables.Patcher;
import me.pajic.sensible_stackables.SensibleStackables;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod(SensibleStackables.MOD_ID)
@EventBusSubscriber(modid = SensibleStackables.MOD_ID)
public class NeoforgeEntrypoint {

	@SubscribeEvent
	private static void onCommonSetup(FMLCommonSetupEvent event) {
		SensibleStackables.onInitialize();
	}

	@SubscribeEvent
	private static void initPatchEvent(TagsUpdatedEvent event) {
		Patcher.patchItems(event./^? if 1.21.1 {^//^getRegistryAccess()^//^?} else {^/getLookupProvider()/^?}^/);
	}
}
*///?}
