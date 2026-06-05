package me.pajic.sensible_stackables.mixin.stack_uncapper.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.sensible_stackables.SensibleStackablesClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract int getCount();

    /**
     * @reason Shows the exact item count in the item tooltip when the item count in the stack is large,
     * as the regular item stack count is abbreviated and no longer shows the exact value.
     */
    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V"
            )
    )
    private void showExactItemCountIfLarge(
			CallbackInfoReturnable<List<Component>> cir,
			@Local(name = "lines") List<Component> lines
	) {
        if (SensibleStackablesClient.CONFIG.itemCountTooltip.get() && SensibleStackablesClient.CONFIG.itemCountShortening.get() && getCount() > 999) {
	        lines.add(Component.translatable("text.sensible_stackables.item_count", getCount()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
