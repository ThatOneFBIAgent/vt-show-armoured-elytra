package iza.armourelytra.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;

public final class RenderHelper {
	private RenderHelper() {
	}

	public static ItemStack getBundledChestplate(ItemStack stack) {
		if (!stack.is(Items.ELYTRA)) {
			return stack;
		}

		BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		if (bundleContents.isEmpty()) {
			return stack;
		}

		for (ItemStackTemplate item : bundleContents.items()) {
			if (item.is(ItemTags.CHEST_ARMOR)) {
				return item.create();
			}
		}

		return stack;
	}

	public static ItemStack getBundledElytra(ItemStack stack) {
		if (!stack.is(Items.ELYTRA)) {
			return stack;
		}

		BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		if (bundleContents.isEmpty()) {
			return stack;
		}

		for (ItemStackTemplate item : bundleContents.items()) {
			if (item.is(Items.ELYTRA)) {
				return item.create();
			}
		}

		return stack;
	}
}
