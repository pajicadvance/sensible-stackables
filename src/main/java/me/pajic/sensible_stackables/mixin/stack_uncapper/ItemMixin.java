package me.pajic.sensible_stackables.mixin.stack_uncapper;

import me.pajic.sensible_stackables.extension.ItemExtension;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
    //~}
}
