package me.pajic.sensible_stackables;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.pajic.sensible_stackables.config.ModConfig;
import me.pajic.sensible_stackables.defaulted.MaxStackSizePatchEvent;
import me.pajic.sensible_stackables.mixson.DynamicTagEvent;
import me.pajic.sensible_stackables.mixson.MixsonHelper;
import me.pajic.sensible_stackables.platform.MultiLoaderUtil;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensibleStackables {

    public static final String MOD_ID = /*$ mod_id*/ "sensible_stackables";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModConfig::new);

    public static void onInitialize() {
        MixsonHelper.setDebugFlags();
        DynamicTagEvent.register();
        MaxStackSizePatchEvent.register();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void debugLog(String message, Object ... args) {
        if (MultiLoaderUtil.INSTANCE.isDevEnv()) LOGGER.info(message, args);
    }
}
