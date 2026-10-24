package me.pajic.sensible_stackables.mixin.stack_uncapper;

//? >=26.1 {

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStackTemplate.class)
public class ItemStackTemplateMixin {

	@Shadow @Final private Holder<Item> item;
	@Shadow @Final private DataComponentPatch components;

	/**
	 * @reason Patches the ItemStackTemplate codec to accept up to the integer max value, up from 99.
	 */
	@ModifyArg(
			method = "lambda$static$0",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
			),
			index = 1
	)
	private static int uncapStackSize(int original) {
		return SensibleStackables.CONFIG.uncapStackSize.get() ? Integer.MAX_VALUE : original;
	}

	@SuppressWarnings({"unchecked", "WrapperTypeMayBePrimitive"})
	@WrapMethod(method = "get")
	private <T> @Nullable T overrideMaxStackSize(DataComponentType<? extends T> type, Operation<T> original) {
        DataComponentPatchAccessor accessor = (DataComponentPatchAccessor) (Object) components;
		if (type == DataComponents.MAX_STACK_SIZE && accessor != null && !accessor.ss$getMap().containsKey(type)) {
			Integer override = StackSizeOverrides.get(item.value());
			if (override > 0) return (T) override;
		}
		return original.call(type);
	}
}
//?}
