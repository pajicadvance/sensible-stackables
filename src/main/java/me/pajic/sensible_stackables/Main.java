package me.pajic.sensible_stackables;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.slf4j.Logger;

import net.neoforged.fml.common.Mod;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

@Mod("sensible_stackables")
@EventBusSubscriber(modid = "sensible_stackables")
public class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger("Sensible Stackables");
    private static final boolean DEBUG = !FMLLoader/*? if > 1.21.1 {*//*.getCurrent()*//*?}*/.isProduction();
    public static NumberFormat FORMATTER;

    public Main() {
        FORMATTER = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        FORMATTER.setParseIntegerOnly(true);
        FORMATTER.setRoundingMode(RoundingMode.DOWN);
    }

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        Main.debugLog("Applying stack sizes");
        patchItems(event./*? if <= 1.21.1 {*/getRegistryAccess()/*?} else {*//*getLookupProvider()*//*?}*/.lookupOrThrow(Registries.ITEM));
    }

    private static void patchItems(HolderLookup.RegistryLookup<Item> registry) {
        ModConfig.CONFIG.items().forEach((s, i) -> {
            if (s.startsWith("#")) {
                ResourceLocation rl = ResourceLocation.tryParse(s.substring(1));
                if (rl != null) registry.get(TagKey.create(Registries.ITEM, rl)).ifPresent(holders -> holders.forEach(
                        itemHolder -> patchItem(itemHolder.value(), i)
                ));
                else LOGGER.error("Item tag {} not found", s);
            } else {
                ResourceLocation rl = ResourceLocation.tryParse(s);
                if (rl != null) registry.get(ResourceKey.create(Registries.ITEM, rl)).ifPresent(
                        item -> patchItem(item.value(), i)
                );
                else LOGGER.error("Item {} not found", s);
            }
        });
    }

    private static void patchItem(Item item, int i) {
        if (!item.components.has(DataComponents.MAX_DAMAGE) && i != item.components.getOrDefault(DataComponents.MAX_STACK_SIZE, 1)) {
            int stackSize = Mth.clamp(i, 1, ModConfig.CONFIG.uncapStackSize() ? Integer.MAX_VALUE : 64);
            item.components = PatchedDataComponentMap.fromPatch(
                    item.components,
                    DataComponentPatch.builder().set(DataComponents.MAX_STACK_SIZE, stackSize).build()
            );
            debugLog("Set stack size for {} to {}", item.getDescriptionId(), stackSize);
        }
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
