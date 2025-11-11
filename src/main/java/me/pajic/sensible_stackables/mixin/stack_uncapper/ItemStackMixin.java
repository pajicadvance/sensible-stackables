package me.pajic.sensible_stackables.mixin.stack_uncapper;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Shadow
    public abstract int getCount();

    /**
     * @reason Patches the ItemStack codec to accept up to the integer max value, up from 99.
     */
    @ModifyArg(
            method = /*? if <= 1.21.1 {*/"lambda$static$3"/*?} else {*//*"lambda$static$1"*//*?}*/,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
            ),
            index = 1
    )
    private static int uncapStackSize(int original) {
        return ModConfig.CONFIG.uncapStackSize() ? Integer.MAX_VALUE : original;
    }

    /**
     * @reason Shows the exact item count in the item tooltip when the item count in the stack is large,
     * as the regular item stack count is abbreviated and no longer shows the exact value.
     */
    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    //? if <= 1.21.1 {
                    target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z",
                    ordinal = 0
                    //?} else {
                    /*target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V"
                    *///?}
            )
    )
    private void showExactItemCountIfLarge(CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (getCount() > 999) list.add(Component.translatable("text.sensible_stackables.item_count", getCount())
                .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
