package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Containers.class)
public class ContainersMixin {

    /**
     * @reason Vanilla hardcodes the amount to split the stack by to 10-30.
     * When the max stack size is set to very high values, breaking containers which have thousands or even millions of items
     * will spawn enough item entities to slow the renderer down to a crawl and effectively deadlock the game.
     * This mixin patches the stack split amount to be based on the item's max stack size instead of a hardcoded value.
     */
    @ModifyArg(
            method = "dropItemStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private static int increaseSplit(
			int original,
			@Local(name = "level", argsOnly = true) Level level,
			@Local(name = "itemStack", argsOnly = true) ItemStack itemStack
	) {
        if (SensibleStackables.CONFIG.uncapStackSize.get()) {
            int batchSize = Math.max(Math.round(itemStack.getMaxStackSize() / 6F), 1);
            return level.getRandom().nextInt(2 * batchSize) + batchSize;
        }
        return original;
    }
}
