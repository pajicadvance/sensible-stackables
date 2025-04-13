package me.pajic.sensible_stackables.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/world/inventory/HorseInventoryMenu$1")
public class HorseInventoryMenuSaddleSlotMixin {

    //? if < 1.21.5 {
    @ModifyReturnValue(
            method = "mayPlace",
            at = @At("RETURN")
    )
    private boolean modifyMayPlace(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original && stack.getCount() == 1;
    }
    //?}
}
