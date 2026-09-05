package me.pajic.sensible_stackables.mixin.client;

import me.pajic.sensible_stackables.SensibleStackablesClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

//? >=26.1 {
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/*import com.llamalad7.mixinextras.injector.ModifyReturnValue;
*///?}

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract int getCount();

    /**
     * @reason Shows the exact item count in the item tooltip when the item count in the stack is large,
     * as the regular item stack count is abbreviated and no longer shows the exact value.
     */
    //? >=26.1 {
    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V"
            )
    )
    private void showExactItemCountIfLarge(
			CallbackInfoReturnable<List<Component>> cir,
			@Local List<Component> lines
	) {
        sensibleStackables$addLine(lines);
    }
    //?} else {
    /*@ModifyReturnValue(
            method = "getTooltipLines",
            at = @At("RETURN")
    )
    private List<Component> showExactItemCountIfLarge(List<Component> lines) {
        sensibleStackables$addLine(lines);
        return lines;
    }
    *///?}

    @Unique private void sensibleStackables$addLine(List<Component> lines) {
        if (SensibleStackablesClient.CONFIG.itemCountTooltip.get() && SensibleStackablesClient.CONFIG.itemCountShortening.get() && getCount() > 999) {
            lines.add(Component.translatable("text.sensible_stackables.item_count", getCount()).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }
}
