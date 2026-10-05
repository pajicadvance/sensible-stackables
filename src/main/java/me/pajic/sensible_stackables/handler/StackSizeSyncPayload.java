package me.pajic.sensible_stackables.handler;

import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record StackSizeSyncPayload(Map<Item, Integer> sizes) implements CustomPacketPayload {

    public static final Type<StackSizeSyncPayload> TYPE = new Type<>(SensibleStackables.id("stack_sizes"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StackSizeSyncPayload> CODEC =
        ByteBufCodecs.<RegistryFriendlyByteBuf, Item, Integer, Map<Item, Integer>>map(
                HashMap::new, ByteBufCodecs.registry(Registries.ITEM), ByteBufCodecs.VAR_INT
        ).map(StackSizeSyncPayload::new, StackSizeSyncPayload::sizes);

    @Override @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
