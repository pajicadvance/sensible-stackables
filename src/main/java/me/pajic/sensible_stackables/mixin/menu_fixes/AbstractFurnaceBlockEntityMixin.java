package me.pajic.sensible_stackables.mixin.menu_fixes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.sensible_stackables.handler.FuelRemainders;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Shadow protected NonNullList<ItemStack> items;

    /**
     * @reason Prevents hoppers from stacking fuel with a crafting remainder, see {@link FurnaceFuelSlotMixin}.
     */
    @ModifyReturnValue(
            method = "canPlaceItem",
            at = @At("RETURN")
    )
    private boolean limitRemainderFuel(boolean original, int slot, ItemStack itemStack) {
        return original && !(slot == 1 && !items.get(1).isEmpty() && FuelRemainders.hasCraftingRemainder(itemStack));
    }
}
