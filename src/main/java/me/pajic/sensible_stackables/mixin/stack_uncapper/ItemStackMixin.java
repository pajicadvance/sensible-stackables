package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.extension.ComponentMapOwner;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? <26.1 {
/*import net.minecraft.world.level.ItemLike;
*///?} else {
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
//?}

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/PatchedDataComponentMap;)V",
                    "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V"
            },
            at = @At("TAIL")
    )
    //~ if <26.1 'Holder<Item>' -> 'ItemLike'
    private void setOwner(Holder<Item> item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        //~ if <26.1 'item.value()' -> 'item.asItem()'
        ((ComponentMapOwner) (Object) components).ss$setOwner(item.value());
    }

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

    /**
     * @reason Recipe results, loot and commands validate stacks before creating them.
     * If an override lowers the max stack size below a recipe's output count, the result would be discarded entirely.
     */
    @ModifyExpressionValue(
            method = "validateStrict",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"
            )
    )
    private static int allowVanillaStackCount(int original, @Local(argsOnly = true) ItemStack itemStack) {
        return StackSizeOverrides.lenientMaxStackSize(itemStack.getItem(), original);
    }

    /**
     * @reason Same as above, for items inside containers, bundles and charged projectiles.
     */
    @ModifyExpressionValue(
            //~ if <26.1 'validateContainedItemSizes' -> 'validateComponents'
            method = "validateContainedItemSizes",
            at = @At(
                    value = "INVOKE",
                    //? <26.1
                    //target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"
                    //? >=26.1
                    target = "Lnet/minecraft/world/item/ItemInstance;getMaxStackSize()I"
            )
    )
    //~ if <26.1 'ItemInstance' -> 'ItemStack'
    private static int allowVanillaContainedStackCount(int original, @Local ItemInstance item) {
        //~ if <26.1 'item.typeHolder().value()' -> 'item.getItem()'
        return StackSizeOverrides.lenientMaxStackSize(item.typeHolder().value(), original);
    }
}
