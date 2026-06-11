package iza.armourelytra.network;

import java.util.Optional;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

public record ArmouredElytraVisual(Identifier itemId, Optional<ArmorTrim> trim, Optional<DyedItemColor> dyedColor, boolean glint) {
	public static final StreamCodec<RegistryFriendlyByteBuf, ArmouredElytraVisual> STREAM_CODEC = StreamCodec.ofMember(
			ArmouredElytraVisual::write,
			ArmouredElytraVisual::read);

	public static ArmouredElytraVisual fromChestplate(ItemStack chestplate) {
		return new ArmouredElytraVisual(
				BuiltInRegistries.ITEM.getKey(chestplate.getItem()),
				Optional.ofNullable(chestplate.get(DataComponents.TRIM)),
				Optional.ofNullable(chestplate.get(DataComponents.DYED_COLOR)),
				chestplate.hasFoil());
	}

	public ItemStack toStack() {
		Item item = BuiltInRegistries.ITEM.getValue(itemId);
		ItemStack stack = new ItemStack(item);
		trim.ifPresent(value -> stack.set(DataComponents.TRIM, value));
		dyedColor.ifPresent(value -> stack.set(DataComponents.DYED_COLOR, value));
		if (glint) {
			stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		}
		return stack;
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		Identifier.STREAM_CODEC.encode(buffer, itemId);
		buffer.writeBoolean(trim.isPresent());
		trim.ifPresent(value -> ArmorTrim.STREAM_CODEC.encode(buffer, value));
		buffer.writeBoolean(dyedColor.isPresent());
		dyedColor.ifPresent(value -> DyedItemColor.STREAM_CODEC.encode(buffer, value));
		buffer.writeBoolean(glint);
	}

	private static ArmouredElytraVisual read(RegistryFriendlyByteBuf buffer) {
		Identifier itemId = Identifier.STREAM_CODEC.decode(buffer);
		Optional<ArmorTrim> trim = buffer.readBoolean() ? Optional.of(ArmorTrim.STREAM_CODEC.decode(buffer)) : Optional.empty();
		Optional<DyedItemColor> dyedColor = buffer.readBoolean() ? Optional.of(DyedItemColor.STREAM_CODEC.decode(buffer)) : Optional.empty();
		boolean glint = buffer.readBoolean();
		return new ArmouredElytraVisual(itemId, trim, dyedColor, glint);
	}
}
