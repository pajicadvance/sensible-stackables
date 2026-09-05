package me.pajic.sensible_stackables.mixin.stack_uncapper;

//? >=26.1 {

import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStackTemplate.class)
public class ItemStackTemplateMixin {

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
}
//?}
