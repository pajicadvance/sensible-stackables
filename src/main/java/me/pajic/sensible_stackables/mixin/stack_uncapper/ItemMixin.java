package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.extension.ItemExtension;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//? <26.1 && fabric
//import org.spongepowered.asm.mixin.Final;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemExtension {

    //~ if >=26.1 'components' -> 'components()' {
    //? >=26.1 {
    @Shadow public abstract DataComponentMap components();
    //?} else {
    /*@Shadow /^? fabric {^//^@Final^//^?}^/ private DataComponentMap components();
    *///?}

    @Override
    public boolean sensible_stackables$isDamageable() {
        return components().has(DataComponents.MAX_DAMAGE) && !components().has(DataComponents.UNBREAKABLE) && components().has(DataComponents.DAMAGE);
    }

    @ModifyReturnValue(
            method = "getDefaultMaxStackSize",
            at = @At("RETURN")
    )
    private int overrideMaxStackSize(int original) {
        int override = StackSizeOverrides.get((Item) (Object) this);
        return override > 0 ? override : original;
    }
    //~}
}
