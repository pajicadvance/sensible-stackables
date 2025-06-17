package me.pajic.sensible_stackables.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Item;<init>(Lnet/minecraft/world/item/Item$Properties;)V"
            )
    )
    private static Item.Properties stackableCake(Item.Properties properties, @Local(argsOnly = true) Block block) {
        if (ModConfig.CONFIG.enableStackableCake && block instanceof CakeBlock) {
            return new Item.Properties()
                //? if >= 1.21.4
                /*.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("cake")))*/
                .stacksTo(ModConfig.CONFIG.cakeMaxStackSize);
        }
        return properties;
    }
}
