3.1.0:

- Added Fabric 26.3 version.
- Defaulted is no longer required.
  - The mod no longer patches item components to change stack sizes, it now keeps track of stack sizes on its own and intercepts stack size calls instead.
  - Configuration is unchanged, item tags are still supported in the config, and stack size changes are still synced to clients.
  - Changing stack sizes in the config in-game now applies changes immediately and doesn't require a datapack reload.
  - Mod compatibility should remain the same, please report any issues if you run into them.
- Mixson is no longer required.
  - It can still be installed for the "All blocks" and "Minecarts" dynamic item tags.
- Added cushions to the default configuration, increasing their stack size from 16 to 64.

3.1.1:

- Fixed stack sizes not clearing properly on config update.
