package iza.armourelytra.client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import iza.armourelytra.network.ArmouredElytraVisual;
import net.minecraft.world.item.ItemStack;

public final class ClientVisualCache {
	private static final Map<Integer, ItemStack> VISUALS = new HashMap<>();

	private ClientVisualCache() {
	}

	public static ItemStack get(int entityId) {
		return VISUALS.getOrDefault(entityId, ItemStack.EMPTY);
	}

	public static void set(int entityId, Optional<ArmouredElytraVisual> visual) {
		if (visual.isPresent()) {
			VISUALS.put(entityId, visual.get().toStack());
		} else {
			VISUALS.remove(entityId);
		}
	}

	public static void clear() {
		VISUALS.clear();
	}
}
