package me.pajic.sensible_stackables.mixin.throw_cooldown;

import me.pajic.sensible_stackables.config.ModConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//? if 1.21.1
//import net.minecraft.world.InteractionResultHolder;
//? if > 1.21.1
import net.minecraft.world.InteractionResult;

@Mixin(ThrowablePotionItem.class)
public class ThrowablePotionItemMixin extends PotionItem {

    public ThrowablePotionItemMixin(Properties properties) {
        super(properties);
    }

    //? if 1.21.1 {
    /*@Inject(
            method = "use",
            at = @At("RETURN")
    )
    private void addThrowingCooldown(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (ModConfig.CONFIG.splashPotionCooldown() > 0) {
            player.getCooldowns().addCooldown(player.getItemInHand(usedHand).getItem(), ModConfig.CONFIG.splashPotionCooldown() * 20);
        }
    }
    *///?} else {
    @Inject(
            method = "use",
            at = @At("RETURN")
    )
    private void addThrowingCooldown(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (ModConfig.CONFIG.splashPotionCooldown() > 0) {
            player.getCooldowns().addCooldown(player.getItemInHand(hand), ModConfig.CONFIG.splashPotionCooldown() * 20);
        }
    }
    //?}
}
