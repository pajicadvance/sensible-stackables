package me.pajic.sensible_stackables.platform;

import java.nio.file.Path;

public interface Platform {

	boolean isModLoaded(String modId);

	ModLoader loader();

	String mcVersion();

	boolean isDevelopmentEnvironment();

	default boolean isDebug() {
		return isDevelopmentEnvironment();
	}

	Path configDir();

	enum ModLoader {
		FABRIC, NEOFORGE
	}
}
