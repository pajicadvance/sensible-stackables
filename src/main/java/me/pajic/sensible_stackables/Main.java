package me.pajic.sensible_stackables;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Main implements ModInitializer {

    public static final String MOD_ID = "sensible_stackables";
    private static final Logger LOGGER = LoggerFactory.getLogger("Sensible Stackables");
    private static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(S2CSyncConfigPayload.TYPE, S2CSyncConfigPayload.CODEC);
        ServerPlayerEvents.JOIN.register(player -> {
            Main.debugLog("Sending stack size data to player {}", player.getName().getString());
            ServerPlayNetworking.send(player, new Main.S2CSyncConfigPayload(
                    ModConfig.CONFIG.items(), ModConfig.CONFIG.splashPotionCooldown(), ModConfig.CONFIG.uncapStackSize())
            );
        });
    }

    public static void patchItems(Level level) {
        Registry<Item> registry = level.registryAccess()./*? if <= 1.21.1 {*/registryOrThrow/*?} else {*//*lookupOrThrow*//*?}*/(Registries.ITEM);
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
        if (!item.components.has(DataComponents.MAX_DAMAGE)) {
            int stackSize = Mth.clamp(i, 1, ModConfig.CONFIG.uncapStackSize() ? Integer.MAX_VALUE : 64);
            item.components = PatchedDataComponentMap.fromPatch(
                    item.components,
                    DataComponentPatch.builder().set(DataComponents.MAX_STACK_SIZE, stackSize).build()
            );
            debugLog("Set stack size for {} to {}", item.getDescriptionId(), stackSize);
        }
    }

    public record S2CSyncConfigPayload(Map<String, Integer> items, int splashPotionCooldown, boolean uncapStackSize) implements CustomPacketPayload {
        public static final Type<S2CSyncConfigPayload> TYPE = new Type<>(withModNamespace("sync_config"));
        public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncConfigPayload> CODEC = CustomPacketPayload.codec(
                S2CSyncConfigPayload::write, S2CSyncConfigPayload::new
        );

        private S2CSyncConfigPayload(RegistryFriendlyByteBuf buf) {
            this(buf.readMap(HashMap::new, FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt), buf.readInt(), buf.readBoolean());
        }

        private void write(RegistryFriendlyByteBuf buf) {
            buf.writeMap(items, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
            buf.writeInt(splashPotionCooldown);
            buf.writeBoolean(uncapStackSize);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static ResourceLocation withModNamespace(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
