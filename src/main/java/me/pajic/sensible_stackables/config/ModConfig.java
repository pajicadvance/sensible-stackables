package me.pajic.sensible_stackables.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.AllowableStrings;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.pajic.sensible_stackables.SensibleStackables;

import java.util.Map;

@Version(version = 1)
public class ModConfig extends Config {

	public ModConfig() {
		super(SensibleStackables.id("config"));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@RequiresAction(action = Action.RELOAD_DATA)
	public ValidatedMap<String, Integer> items = (new ValidatedMap.Builder())
			.keyHandler(new ValidatedString("", new AllowableStrings(ItemSuggestions.get()::contains, ItemSuggestions::get)))
			.valueHandler(new ValidatedInt(64, Integer.MAX_VALUE, 1))
			.defaults(Map.ofEntries(
					Map.entry("minecraft:enchanted_book", 64),
					Map.entry("minecraft:snowball", 64),
					Map.entry("minecraft:ender_pearl", 64),
					Map.entry("minecraft:saddle", 16),
					Map.entry("minecraft:cake", 16),
					Map.entry("#minecraft:boats", 16),
					Map.entry("#minecraft:beds", 16),
					Map.entry("#minecraft:harnesses", 16),
					Map.entry("#c:eggs", 64),
					Map.entry("#c:potions", 3),
					Map.entry("#c:foods/soup", 16),
					Map.entry("#c:music_discs", 64),
					Map.entry("#c:armors/horse", 64),
					Map.entry("#sensible_stackables:minecarts", 16),
					Map.entry("#sensible_stackables:banner_patterns", 64)
			))
			.build();
	public ValidatedInt splashPotionCooldown = new ValidatedInt(1, Integer.MAX_VALUE, 0);
	@RequiresAction(action = Action.RESTART) public ValidatedBoolean uncapStackSize = new ValidatedBoolean(false);
	@RequiresAction(action = Action.RELOAD_DATA) public ValidatedInt commonStackSize = new ValidatedInt(64, Integer.MAX_VALUE, 1);
}
