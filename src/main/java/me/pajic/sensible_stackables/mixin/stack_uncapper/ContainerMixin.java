package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Container.class)
public interface ContainerMixin {

    /**
     * @reason Uncaps the default max stack size for item stacks in inventory slots.
     */
    @ModifyReturnValue(
            method = "getMaxStackSize()I",
            at = @At("RETURN")
    )
    private int uncapStackSize(int original) {
        return SensibleStackables.CONFIG.uncapStackSize.get() ? Integer.MAX_VALUE : original;
    }
}
