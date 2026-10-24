package me.pajic.sensible_stackables.mixin.menu_fixes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.sensible_stackables.handler.FuelRemainders;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceFuelSlot.class)
public class FurnaceFuelSlotMixin {

    @WrapMethod(method = "isBucket")
    private static boolean limitStack(ItemStack itemStack, Operation<Boolean> original) {
        return itemStack.getCount() <= 1 && original.call(itemStack);
    }

    /**
     * @reason Prevents losing crafting remainders when burning stacked fuel.
     */
    @ModifyReturnValue(
            method = "getMaxStackSize",
            at = @At("RETURN")
    )
    private int limitRemainderFuel(int original, ItemStack itemStack) {
        return FuelRemainders.hasCraftingRemainder(itemStack) ? 1 : original;
    }
}
