package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.extension.ComponentMapOwner;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

//? <26.3
//import java.util.Optional;

@Mixin(PatchedDataComponentMap.class)
public class PatchedDataComponentMapMixin implements ComponentMapOwner {

    //~ if >26.2 'Optional<?>>' -> 'Object>'
    @Shadow private Reference2ObjectMap<DataComponentType<?>, Object> patch;
    @Unique private @Nullable Item ss$owner;

    @SuppressWarnings({"unchecked", "WrapperTypeMayBePrimitive"})
    @WrapMethod(method = "get")
    private <T> @Nullable T overrideMaxStackSize(DataComponentType<? extends T> type, Operation<T> original) {
        if (type == DataComponents.MAX_STACK_SIZE && ss$owner != null && !patch.containsKey(type)) {
            Integer override = StackSizeOverrides.get(ss$owner);
            if (override > 0) return (T) override;
        }
        return original.call(type);
    }

    @Override
    public void ss$setOwner(Item item) {
        this.ss$owner = item;
    }
}
