package iza.armourelytra.client;

import iza.armourelytra.client.network.ClientVisualCache;
import iza.armourelytra.client.network.ClientVisualSender;
import iza.armourelytra.network.ArmouredElytraNetworking;
import iza.armourelytra.network.ArmouredElytraVisualSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class VTShowArmouredElytraClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ArmouredElytraNetworking.registerPayloads();
		ClientPlayNetworking.registerGlobalReceiver(ArmouredElytraVisualSyncPayload.TYPE, (payload, context) -> ClientVisualCache.set(payload.entityId(), payload.visual()));
		ClientTickEvents.END_CLIENT_TICK.register(ClientVisualSender::tick);
		ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
			ClientVisualCache.clear();
			ClientVisualSender.reset();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> {
			ClientVisualCache.clear();
			ClientVisualSender.reset();
		});
	}
}

