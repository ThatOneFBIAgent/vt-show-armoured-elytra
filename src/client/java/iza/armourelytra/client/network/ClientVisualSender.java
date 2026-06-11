package iza.armourelytra.client.network;

import java.util.Optional;

import iza.armourelytra.client.RenderHelper;
import iza.armourelytra.network.ArmouredElytraVisual;
import iza.armourelytra.network.ArmouredElytraVisualUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ClientVisualSender {
	private static Optional<ArmouredElytraVisual> lastSent;

	private ClientVisualSender() {
	}

	public static void reset() {
		lastSent = null;
	}

	public static void tick(Minecraft client) {
		if (client.player == null || !ClientPlayNetworking.canSend(ArmouredElytraVisualUpdatePayload.TYPE)) {
			return;
		}

		Optional<ArmouredElytraVisual> visual = getEquippedVisual(client.player.getItemBySlot(EquipmentSlot.CHEST));
		if (!visual.equals(lastSent)) {
			ClientPlayNetworking.send(new ArmouredElytraVisualUpdatePayload(visual));
			lastSent = visual;
		}
	}

	private static Optional<ArmouredElytraVisual> getEquippedVisual(ItemStack stack) {
		if (!stack.is(Items.ELYTRA)) {
			return Optional.empty();
		}

		ItemStack chestplate = RenderHelper.getBundledChestplate(stack);
		if (chestplate == stack || !chestplate.is(ItemTags.CHEST_ARMOR)) {
			return Optional.empty();
		}

		return Optional.of(ArmouredElytraVisual.fromChestplate(chestplate));
	}
}
