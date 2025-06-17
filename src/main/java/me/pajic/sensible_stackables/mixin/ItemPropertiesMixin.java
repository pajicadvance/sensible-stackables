package me.pajic.sensible_stackables.mixin;

import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin {

    //? if > 1.21.4 {
    /*@ModifyArg(
            method = "horseArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item$Properties;stacksTo(I)Lnet/minecraft/world/item/Item$Properties;"
            )
    )
    private int modifyHorseArmorStackSize(int original) {
        if (ModConfig.CONFIG.enableStackableHorseArmor) {
            int stackSize = ModConfig.CONFIG.horseArmorMaxStackSize;
            if (stackSize > 0 && stackSize <= 64) return stackSize;
        }
        return original;
    }
    *///?}
}
