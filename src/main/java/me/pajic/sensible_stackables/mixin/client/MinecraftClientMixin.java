package me.pajic.sensible_stackables.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.sensible_stackables.config.ItemSuggestions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

	@Inject(
			method = "updateLevelInEngines(Lnet/minecraft/client/multiplayer/ClientLevel;Z)V",
			at = @At("TAIL")
	)
	private void afterClientWorldChange(CallbackInfo ci) {
		ItemSuggestions.update();
	}
}
