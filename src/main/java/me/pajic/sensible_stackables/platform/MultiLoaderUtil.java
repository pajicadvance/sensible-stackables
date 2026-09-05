package me.pajic.sensible_stackables.platform;

//$ loader_util_import
import me.pajic.sensible_stackables.platform.fabric.FabricLoaderUtil;

public interface MultiLoaderUtil {
    MultiLoaderUtil INSTANCE = /*$ loader_util_inst*/ new FabricLoaderUtil();

    boolean isModLoaded(String modId);
    boolean isDevEnv();
}
