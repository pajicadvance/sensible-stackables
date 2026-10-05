package me.pajic.sensible_stackables.mixin.stack_uncapper;

import me.pajic.sensible_stackables.SensibleStackables;
import me.pajic.sensible_stackables.extension.ComponentMapOwner;
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
}
