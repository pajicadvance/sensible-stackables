package me.pajic.sensible_stackables.mixin.menu_fixes;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FurnaceFuelSlot.class)
public class FurnaceFuelSlotMixin {

    @WrapMethod(method = "isBucket")
    private static boolean limitStack(ItemStack itemStack, Operation<Boolean> original) {
        return itemStack.getCount() <= 1 && original.call(itemStack);
    }
}
