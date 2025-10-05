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

Additionally, splash and lingering potions have a 1-second use cooldown to prevent potion throwing spam, and the anvil is patched to support operations with stacks of enchanted books.

Fabric version requires Fabric API. NeoForge version has no dependencies.

## Configuration

Stack size values and the throwing potion cooldown can be configured in `sensible_stackables_client.json` (or `sensible_stackables_server.json` on a dedicated server) inside the config folder. When connecting to a dedicated or LAN server, the client will receive and use the server's configuration file. In singleplayer worlds, the client configuration is used. 

The configuration supports defining stack sizes for either single items or item tags, for example:
- Adding `"minecraft:totem_of_undying": 16` will allow stacking totems up to 16
- Adding `"#minecraft:banners": 64` will allow stacking all items under the `banners` tag up to 64 (# indicates to the mod that this is an item tag)
- Adding `"#c:buckets": 64` will allow stacking all types of buckets up to 64 (`c` is the namespace for conventional tags, see available conventional tags [here](https://wiki.fabricmc.net/community:common_tags))

Items with durability will be ignored. Stack size values more than 64 or less than 1 and will be ignored, unless `uncapStackSize` is set to `true` in the configuration, which allows any value (not recommended for regular gameplay).