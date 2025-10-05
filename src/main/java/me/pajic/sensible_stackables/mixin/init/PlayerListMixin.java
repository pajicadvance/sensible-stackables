package me.pajic.sensible_stackables.mixin.init;

import me.pajic.sensible_stackables.Main;
import me.pajic.sensible_stackables.ModConfig;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    private void onPlayerJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        if (server.isPublished()) {
            Main.debugLog("Sending stack size data to player {}", player.getName().getString());
            PacketDistributor.sendToPlayer(player, new Main.S2CSyncConfigPayload(
                    ModConfig.CONFIG.items(), ModConfig.CONFIG.splashPotionCooldown(), ModConfig.CONFIG.uncapStackSize())
            );
        }
    }
}
