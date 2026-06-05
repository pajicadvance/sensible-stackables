package me.pajic.sensible_stackables.mixin.stack_uncapper;

import me.pajic.sensible_stackables.defaulted.MaxStackSizePatchEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin {

	@Shadow @Nullable private ResourceKey<Item> id;
	@Unique private int ss$maxStackSize = 64;

	@Inject(
			method = "stacksTo",
			at = @At("HEAD")
	)
	private void updateMaxStackSize(int max, CallbackInfoReturnable<Item.Properties> cir) {
		ss$maxStackSize = max;
	}

	@Inject(
			method = "finalizeInitializer",
			at = @At("HEAD")
	)
	private void flagDefaultStackSizeItem(CallbackInfoReturnable<Item.Properties> cir) {
		if (ss$maxStackSize == 64) MaxStackSizePatchEvent.itemsWithDefaultStackSize.add(id);
	}
}
