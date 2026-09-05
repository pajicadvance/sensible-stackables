package me.pajic.sensible_stackables.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.pajic.sensible_stackables.SensibleStackables;

@Version(version = 1)
public class ModClientConfig extends Config {

	public ModClientConfig() {
		super(SensibleStackables.id("client_config"));
	}

	public ValidatedBoolean itemCountShortening = new ValidatedBoolean();
	public ValidatedBoolean itemCountScaling = new ValidatedBoolean();
	public ValidatedBoolean itemCountTooltip = new ValidatedBoolean();
}
