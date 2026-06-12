package iza.armourelytra.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class ArmouredElytraServerRelay {
	private static final Map<UUID, ArmouredElytraVisual> VISUALS = new HashMap<>();
	private static final Set<UUID> INITIAL_SYNC_SENT = new HashSet<>();

	private ArmouredElytraServerRelay() {
	}

	public static void register() {
		ServerPlayNetworking.registerGlobalReceiver(ArmouredElytraVisualUpdatePayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			if (payload.visual().isPresent()) {
				VISUALS.put(player.getUUID(), payload.visual().get());
			} else {
				VISUALS.remove(player.getUUID());
			}

			sendInitialSync(player, context.server());
			relay(player, payload.visual());
		});

		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> sendInitialSync(listener.player, server));

		ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
			UUID playerId = listener.player.getUUID();
			VISUALS.remove(playerId);
			INITIAL_SYNC_SENT.remove(playerId);
			ArmouredElytraVisualSyncPayload payload = new ArmouredElytraVisualSyncPayload(listener.player.getId(), Optional.empty());
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (player != listener.player && ServerPlayNetworking.canSend(player, ArmouredElytraVisualSyncPayload.TYPE)) {
					ServerPlayNetworking.send(player, payload);
				}
			}
		});
	}

	private static void sendInitialSync(ServerPlayer target, MinecraftServer server) {
		if (!INITIAL_SYNC_SENT.add(target.getUUID())) {
			return;
		}

		if (!ServerPlayNetworking.canSend(target, ArmouredElytraVisualSyncPayload.TYPE)) {
			INITIAL_SYNC_SENT.remove(target.getUUID());
			return;
		}

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			ArmouredElytraVisual visual = VISUALS.get(player.getUUID());
			if (visual != null) {
				ServerPlayNetworking.send(target, new ArmouredElytraVisualSyncPayload(player.getId(), Optional.of(visual)));
			}
		}
	}

	private static void relay(ServerPlayer source, Optional<ArmouredElytraVisual> visual) {
		ArmouredElytraVisualSyncPayload payload = new ArmouredElytraVisualSyncPayload(source.getId(), visual);
		for (ServerPlayer player : source.level().getServer().getPlayerList().getPlayers()) {
			if (ServerPlayNetworking.canSend(player, ArmouredElytraVisualSyncPayload.TYPE)) {
				ServerPlayNetworking.send(player, payload);
			}
		}
	}
}
