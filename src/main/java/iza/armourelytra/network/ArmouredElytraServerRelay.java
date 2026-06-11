package iza.armourelytra.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ArmouredElytraServerRelay {
	private static final Map<UUID, ArmouredElytraVisual> VISUALS = new HashMap<>();

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

			relay(player, payload.visual());
		});

		ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
			ServerPlayer joiningPlayer = listener.player;
			if (!ServerPlayNetworking.canSend(joiningPlayer, ArmouredElytraVisualSyncPayload.TYPE)) {
				return;
			}

			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				ArmouredElytraVisual visual = VISUALS.get(player.getUUID());
				if (visual != null) {
					ServerPlayNetworking.send(joiningPlayer, new ArmouredElytraVisualSyncPayload(player.getId(), Optional.of(visual)));
				}
			}
		});

		ServerPlayConnectionEvents.DISCONNECT.register((listener, server) -> {
			VISUALS.remove(listener.player.getUUID());
			ArmouredElytraVisualSyncPayload payload = new ArmouredElytraVisualSyncPayload(listener.player.getId(), Optional.empty());
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (player != listener.player && ServerPlayNetworking.canSend(player, ArmouredElytraVisualSyncPayload.TYPE)) {
					ServerPlayNetworking.send(player, payload);
				}
			}
		});
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

