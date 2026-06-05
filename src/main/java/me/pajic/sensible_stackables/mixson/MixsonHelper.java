package me.pajic.sensible_stackables.mixson;

import com.google.gson.JsonElement;
import me.pajic.sensible_stackables.SensibleStackables;
import net.ramixin.mixson.Mixson;
import net.ramixin.mixson.MixsonCodecs;
import net.ramixin.mixson.enums.DebugOption;
import net.ramixin.mixson.enums.ErrorPolicy;
import net.ramixin.mixson.enums.Lifetime;
import net.ramixin.mixson.util.Index;
import net.ramixin.mixson.util.functions.Event;

public class MixsonHelper {

	private static final ErrorPolicy ERROR_POLICY = SensibleStackables.xplat().isDebug() ? ErrorPolicy.THROW : ErrorPolicy.LOG;

	public static void setDebugFlags() {
		if (SensibleStackables.xplat().isDebug()) {
			Mixson.enableDebugOption(DebugOption.BASIC_LOGGING);
			Mixson.enableDebugOption(DebugOption.EXTRA_LOGGING);
			Mixson.enableDebugOption(DebugOption.EXPORT_PATCHED_FILE);
		}
	}

	public static void registerSingleJson(String eventName, Index target, Event<JsonElement> event) {
		Mixson.registerEvent(
				MixsonCodecs.JSON_ELEMENT,
				Mixson.DEFAULT_PRIORITY,
				Lifetime.PERSISTENT,
				ERROR_POLICY,
				eventName,
				index -> index.idEquals(target),
				event
		);
	}
}
