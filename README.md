# VT Show Armoured Elytra

Shows Vanilla Tweaks combined armoured elytra as both an elytra and a chestplate on clients with the mod installed.

## Multiplayer Trim Sync

For chestplate trims/dye/glint from other players, the server must also have this mod installed. The server side is only a tiny relay: it stores the current visual chestplate data that modded clients send, then forwards it to other modded clients.

Players without the mod can still join normally; they just will not send or receive the extra visual data.

## Server Setup

1. Install Fabric Loader on the server for the same Minecraft version.
2. Put Fabric API in the server `mods` folder.
3. Put `vt-show-armoured-elytra-1.0.0.jar` in the server `mods` folder.
4. Restart the server.
5. Put the same jar in each player/client `mods` folder if they want to see the corrected chestplate visuals.

The jar to share is built at `build/libs/vt-show-armoured-elytra-1.0.0.jar` after running `./gradlew build` or `gradlew.bat build`.
