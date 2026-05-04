package me.pajic.sensible_stackables;

import me.pajic.sensible_stackables.platform.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

//? fabric {
import me.pajic.sensible_stackables.platform.fabric.FabricPlatform;
//?} neoforge {
/*import me.pajic.sensible_stackables.platform.neoforge.NeoforgePlatform;
*///?}

@SuppressWarnings("LoggingSimilarMessage")
public class SensibleStackables {

	public static final String MOD_ID = /*$ mod_id*/ "sensible_stackables";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();

	public static NumberFormat FORMATTER;

	public static void onInitialize() {
		FORMATTER = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
		FORMATTER.setParseIntegerOnly(true);
		FORMATTER.setRoundingMode(RoundingMode.DOWN);
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		*///?}
	}

	public static void debugLog(String message, Object ... args) {
		if (PLATFORM.isDebug()) LOGGER.info(message, args);
	}
}
