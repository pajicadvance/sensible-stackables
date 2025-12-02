# Sensible Stackables

This mod allows increasing the stack size of items, while providing balanced defaults:

- Potions: 3
- Saddle: 16
- Harness: 16
- Boats: 16
- Minecarts: 16
- Beds: 16
- Stews and soups: 16
- Enchanted books: 64
- Ender pearls: 64
- Snowball: 64
- Egg: 64
- Horse armor: 64
- Music discs: 64
- Banner patterns: 64
- Cake: 16

Additionally, splash and lingering potions have a 1-second use cooldown to prevent potion throwing spam, and several menus have been patched to support stacks of items.

The mod can also be used to uncap the vanilla stack size limit and set any value you want, read below for instructions.

Fabric version requires Fabric API. NeoForge version has no dependencies.

## Configuration

Stack size values and the throwing potion cooldown can be configured in `sensible_stackables.json` inside the config folder. If setting up a dedicated server, make sure that the configs on the server and client match or else there will be desync issues.

The configuration supports defining stack sizes for either single items or item tags, for example:
- Adding `"minecraft:totem_of_undying": 16` will allow stacking totems up to 16
- Adding `"#minecraft:banners": 64` will allow stacking all items under the `banners` tag up to 64 (# indicates to the mod that this is an item tag)
- Adding `"#c:buckets": 64` will allow stacking all types of buckets up to 64 (`c` is the namespace for conventional tags, see available conventional tags [here](https://wiki.fabricmc.net/community:common_tags))

## Uncapping max stack size

Setting `uncapStackSize` to `true` in the mod configuration will allow setting stack sizes above 64 to items and apply necessary patches to prevent bugs with large stack sizes. Item counts larger than 999 will be shortened in the GUI and the item tooltip will display the exact item count of the stack.

`commonStackSize` can then be increased from 64 to a higher value to set a "global" max stack size for all items which use the default stack size. Stack sizes for items which don't use the default stack size (such as potions, boats, enchanted books etc.) still need to be defined manually.

**WARNING**: While theoretically you can set max stack sizes for everything up to around 2.1 billion, using sizes larger than 100000 can cause performance issues. For example crafting 100000+ items at the same time or taking out 100000+ items from the stonecutter will freeze the game for a while (5-20 seconds). I've tested the mod with `commonStackSize` set to 32768 and performance was still reasonable. **USE HIGHER VALUES AT YOUR OWN RISK!** 
