package me.pajic.sensible_stackables.mixin.stack_uncapper;

import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.core.component.DataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DataComponents.class)
public class DataComponentsMixin {

    /**
     * @reason Patches the max stack size data component to accept up to the integer max value, up from 99.
     */
    @ModifyArg(
            method = {"lambda$static$1", "method_58570"},
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
