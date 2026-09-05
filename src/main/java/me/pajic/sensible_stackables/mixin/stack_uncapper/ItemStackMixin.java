package me.pajic.sensible_stackables.mixin.stack_uncapper;

import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    /**
     * @reason Patches the ItemStack codec to accept up to the integer max value, up from 99.
     */
    @ModifyArg(
			method = {"lambda$static$1", "method_57371", "lambda$static$3"},
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
