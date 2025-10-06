package me.pajic.sensible_stackables;

import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod(value = Main.MOD_ID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.DEDICATED_SERVER)
public class ServerMain {

    public ServerMain() {}

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        Main.debugLog("Applying stack sizes");
        ModConfig.loadConfig();
        Main.patchItems(event./*? if <= 1.21.1 {*/getRegistryAccess()/*?} else {*//*getLookupProvider()*//*?}*/.lookupOrThrow(Registries.ITEM));
    }
}
