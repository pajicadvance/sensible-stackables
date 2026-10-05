package me.pajic.sensible_stackables;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.pajic.sensible_stackables.config.ModClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class SensibleStackablesClient {

	public static ModClientConfig CONFIG = ConfigApiJava.registerAndLoadConfig(ModClientConfig::new, RegisterType.CLIENT);
	public static NumberFormat FORMATTER;

	public static void onInitialize() {
		FORMATTER = NumberFormat.getCompactNumberInstance(Locale.getDefault(Locale.Category.FORMAT), NumberFormat.Style.SHORT);
		FORMATTER.setParseIntegerOnly(true);
		FORMATTER.setRoundingMode(RoundingMode.DOWN);
	}

    public static void onUpdateConfig() {
        IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) server.execute(() -> SensibleStackables.onUpdateConfig(server));
    }
}
