package iza.armourelytra.client;

import net.minecraft.core.component.DataComponentType;
import iza.armourelytra.client.network.ClientVisualCache;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;

public final class RenderHelper {
	private RenderHelper() {
	}

	public static ItemStack getChestplateForRender(ItemStack stack, HumanoidRenderState state) {
		if (state instanceof AvatarRenderState avatarRenderState) {
			ItemStack cachedStack = ClientVisualCache.get(avatarRenderState.id);
			if (!cachedStack.isEmpty()) {
				return cachedStack;
			}
		}

		return getBundledChestplate(stack);
	}

	public static ItemStack getBundledChestplate(ItemStack stack) {
		if (!stack.is(Items.ELYTRA)) {
			return stack;
		}

		BundleContents bundleContents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		if (!bundleContents.isEmpty()) {
			for (ItemStackTemplate item : bundleContents.items()) {
				if (item.is(ItemTags.CHEST_ARMOR)) {
					return item.create();
				}
			}
		}

		ItemStack syncedChestplate = getSyncedChestplate(stack);
		if (!syncedChestplate.isEmpty()) {
			return syncedChestplate;
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

	private static ItemStack getSyncedChestplate(ItemStack stack) {
		Item armorItem = getSyncedChestplateItem(stack);
		if (armorItem == null) {
			return ItemStack.EMPTY;
		}

		ItemStack chestplate = new ItemStack(armorItem);
		copyComponent(stack, chestplate, DataComponents.TRIM);
		copyComponent(stack, chestplate, DataComponents.DYED_COLOR);
		copyComponent(stack, chestplate, DataComponents.ENCHANTMENTS);
		copyComponent(stack, chestplate, DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
		return chestplate;
	}

	private static Item getSyncedChestplateItem(ItemStack stack) {
		Identifier itemModel = stack.get(DataComponents.ITEM_MODEL);
		Item item = getChestplateItem(itemModel == null ? null : itemModel.toString());
		if (item != null) {
			return item;
		}

		CustomModelData customModelData = stack.get(DataComponents.CUSTOM_MODEL_DATA);
		if (customModelData != null) {
			for (String string : customModelData.strings()) {
				item = getChestplateItem(string);
				if (item != null) {
					return item;
				}
			}
		}

		ItemLore lore = stack.get(DataComponents.LORE);
		if (lore != null) {
			for (var line : lore.lines()) {
				item = getChestplateItem(line.getString());
				if (item != null) {
					return item;
				}
			}
		}

		return getChestplateItemFromAttributes(stack);
	}

	private static Item getChestplateItem(String value) {
		if (value == null) {
			return null;
		}

		String normalized = value.toLowerCase();
		if (normalized.contains("netherite_chestplate") || normalized.contains("netherite chestplate")) {
			return Items.NETHERITE_CHESTPLATE;
		}
		if (normalized.contains("diamond_chestplate") || normalized.contains("diamond chestplate")) {
			return Items.DIAMOND_CHESTPLATE;
		}
		if (normalized.contains("iron_chestplate") || normalized.contains("iron chestplate")) {
			return Items.IRON_CHESTPLATE;
		}
		if (normalized.contains("golden_chestplate") || normalized.contains("gold_chestplate") || normalized.contains("golden chestplate")) {
			return Items.GOLDEN_CHESTPLATE;
		}
		if (normalized.contains("chainmail_chestplate") || normalized.contains("chainmail chestplate")) {
			return Items.CHAINMAIL_CHESTPLATE;
		}
		if (normalized.contains("copper_chestplate") || normalized.contains("copper chestplate")) {
			return Items.COPPER_CHESTPLATE;
		}
		if (normalized.contains("leather_chestplate") || normalized.contains("leather tunic")) {
			return Items.LEATHER_CHESTPLATE;
		}

		return null;
	}

	private static Item getChestplateItemFromAttributes(ItemStack stack) {
		ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
		if (modifiers == null) {
			return null;
		}

		double armor = 0.0;
		double toughness = 0.0;
		double knockbackResistance = 0.0;
		for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
			String attribute = entry.attribute().getRegisteredName();
			if (attribute.endsWith("armor")) {
				armor = Math.max(armor, entry.modifier().amount());
			} else if (attribute.endsWith("armor_toughness")) {
				toughness = Math.max(toughness, entry.modifier().amount());
			} else if (attribute.endsWith("knockback_resistance")) {
				knockbackResistance = Math.max(knockbackResistance, entry.modifier().amount());
			}
		}

		if (armor >= 8.0 && knockbackResistance > 0.0) {
			return Items.NETHERITE_CHESTPLATE;
		}
		if (armor >= 8.0) {
			return Items.DIAMOND_CHESTPLATE;
		}
		if (armor >= 6.0) {
			return Items.IRON_CHESTPLATE;
		}
		if (armor >= 5.0 && toughness > 0.0) {
			return Items.COPPER_CHESTPLATE;
		}
		if (armor >= 5.0) {
			return Items.CHAINMAIL_CHESTPLATE;
		}
		if (armor >= 3.0) {
			return Items.LEATHER_CHESTPLATE;
		}

		return null;
	}

	private static <T> void copyComponent(ItemStack from, ItemStack to, DataComponentType<T> componentType) {
		T value = from.get(componentType);
		if (value != null) {
			to.set(componentType, value);
		}
	}
}

