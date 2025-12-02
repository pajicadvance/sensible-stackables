package me.pajic.sensible_stackables.platform;

import java.nio.file.Path;

public interface Platform {
	boolean isModLoaded(String modId);

	boolean isDebug();

	ModLoader loader();

	String mcVersion();

	Path configDir();

	enum ModLoader {
		FABRIC, NEOFORGE, FORGE
	}
}
