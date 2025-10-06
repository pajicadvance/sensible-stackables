package me.pajic.sensible_stackables;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import net.neoforged.fml.common.Mod;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "sensible_stackables";
    private static final Logger LOGGER = LoggerFactory.getLogger("Sensible Stackables");
    private static final boolean DEBUG = !FMLLoader/*? if > 1.21.1 {*//*.getCurrent()*//*?}*/.isProduction();

    public Main(IEventBus modEventBus) {
        modEventBus.addListener(this::registerPayload);
    }

    public static void patchItems(HolderLookup.RegistryLookup<Item> registry) {
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

    @SubscribeEvent
    public void registerPayload(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                Main.S2CSyncConfigPayload.TYPE,
                Main.S2CSyncConfigPayload.CODEC,
                (payload, context) -> {
                    Main.debugLog("Applying stack sizes received from server");
                    ModConfig.CONFIG = new ModConfig.Config(payload.items(), payload.splashPotionCooldown(), payload.uncapStackSize());
                    Main.patchItems(context.player().level().registryAccess().lookupOrThrow(Registries.ITEM));
                }
        );
    }

    public static ResourceLocation withModNamespace(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (DEBUG) LOGGER.info(message, args);
    }
}
