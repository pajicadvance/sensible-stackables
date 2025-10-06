package me.pajic.sensible_stackables;

import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TagsUpdatedEvent;

@Mod(value = Main.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ClientMain {

    public ClientMain() {}

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        Main.debugLog("Applying stack sizes from local configuration");
        ModConfig.loadConfig();
        Main.patchItems(event./*? if <= 1.21.1 {*/getRegistryAccess()/*?} else {*//*getLookupProvider()*//*?}*/.lookupOrThrow(Registries.ITEM));
    }
}
