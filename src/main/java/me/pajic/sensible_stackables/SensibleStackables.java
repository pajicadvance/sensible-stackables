package me.pajic.sensible_stackables;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.sensible_stackables.config.ModConfig;
import me.pajic.sensible_stackables.handler.StackSizeOverrides;
import me.pajic.sensible_stackables.handler.StackSizeSyncPayload;
import me.pajic.sensible_stackables.mixson.DynamicTagEvent;
import me.pajic.sensible_stackables.platform.MultiLoaderUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensibleStackables {

    public static final String MOD_ID = /*$ mod_id*/ "sensible_stackables";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
        if (MultiLoaderUtil.INSTANCE.isModLoaded("mixson") ||
            MultiLoaderUtil.INSTANCE.isModLoaded("mixson_backport")) DynamicTagEvent.register();
    }

    public static void onUpdateConfig(MinecraftServer server) {
        StackSizeOverrides.computeAndSet(server.registryAccess());
        var payload = new StackSizeSyncPayload(StackSizeOverrides.snapshot());
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            MultiLoaderUtil.INSTANCE.s2c(player, payload);
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
