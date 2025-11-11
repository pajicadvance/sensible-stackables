package me.pajic.sensible_stackables;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Main implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("Sensible Stackables");
    private static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static NumberFormat FORMATTER;

    @Override
    public void onInitialize() {
        FORMATTER = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        FORMATTER.setParseIntegerOnly(true);
        FORMATTER.setRoundingMode(RoundingMode.DOWN);
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            Main.debugLog("Applying stack sizes");
            Main.patchItems(registries);
        });
    }

    private static void patchItems(RegistryAccess registryAccess) {
        Registry<Item> registry = registryAccess./*? if <= 1.21.1 {*/registryOrThrow/*?} else {*//*lookupOrThrow*//*?}*/(Registries.ITEM);
        ModConfig.CONFIG.items().forEach((s, i) -> {
            if (s.startsWith("#")) {
                ResourceLocation rl = ResourceLocation.tryParse(s.substring(1));
                if (rl != null) registry.getTagOrEmpty(TagKey.create(Registries.ITEM, rl)).forEach(
                        itemHolder -> patchItem(itemHolder.value(), i)
                );
                else LOGGER.error("Item tag {} not found", s);
            } else {
                ResourceLocation rl = ResourceLocation.tryParse(s);
                if (rl != null) registry.getOptional(rl).ifPresent(item -> patchItem(item, i));
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
