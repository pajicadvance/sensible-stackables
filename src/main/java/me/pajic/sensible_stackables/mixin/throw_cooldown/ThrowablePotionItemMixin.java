package me.pajic.sensible_stackables.mixin.throw_cooldown;

import me.pajic.sensible_stackables.SensibleStackables;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.InteractionResult;

@Mixin(ThrowablePotionItem.class)
public class ThrowablePotionItemMixin extends PotionItem {

    public ThrowablePotionItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "use",
            at = @At("RETURN")
    )
    private void addThrowingCooldown(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		int cooldown = SensibleStackables.CONFIG.splashPotionCooldown.get();
        if (cooldown > 0) player.getCooldowns().addCooldown(player.getItemInHand(hand)/*? <26.1 {*//*.getItem()*//*?}*/, cooldown * 20);
    }
}
