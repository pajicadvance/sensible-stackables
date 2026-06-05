package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    /**
     * @reason Allows item entity merging to merge more than 64 items at once.
     */
    @ModifyExpressionValue(
            method = "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=64"
            )
    )
    private static int uncapMergeSize(int original, @Local(name = "fromStack", argsOnly = true) ItemStack fromStack) {
        return SensibleStackables.CONFIG.uncapStackSize.get() ? fromStack.getMaxStackSize() : original;
    }
}
