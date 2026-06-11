package iza.armourelytra.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class ArmouredElytraNetworking {
	private static boolean registered;

	private ArmouredElytraNetworking() {
	}

	public static void registerPayloads() {
		if (registered) {
			return;
		}

		PayloadTypeRegistry.serverboundPlay().register(ArmouredElytraVisualUpdatePayload.TYPE, ArmouredElytraVisualUpdatePayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(ArmouredElytraVisualSyncPayload.TYPE, ArmouredElytraVisualSyncPayload.CODEC);
		registered = true;
	}
}
