package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.sensible_stackables.ModConfig;
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
            method = "method_58570",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
            ),
            index = 1
    )
    private static int uncapStackSize(int original) {
        return ModConfig.CONFIG.uncapStackSize() ? Integer.MAX_VALUE : original;
    }

    /**
     * @reason Sets the max stack size of all items which use the default stack size to the value defined in the config.
     */
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=64"
            )
    )
    private static int setCommonStackSize(int original) {
        return ModConfig.CONFIG.uncapStackSize() ? ModConfig.CONFIG.commonStackSize() : original;
    }
}
