package iza.armourelytra.network;

import java.util.Optional;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ArmouredElytraVisualUpdatePayload(Optional<ArmouredElytraVisual> visual) implements CustomPacketPayload {
	public static final Type<ArmouredElytraVisualUpdatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("vt-show-armoured-elytra", "update"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ArmouredElytraVisualUpdatePayload> CODEC = StreamCodec.ofMember(
			ArmouredElytraVisualUpdatePayload::write,
			ArmouredElytraVisualUpdatePayload::read);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		buffer.writeBoolean(visual.isPresent());
		visual.ifPresent(value -> ArmouredElytraVisual.STREAM_CODEC.encode(buffer, value));
	}

	private static ArmouredElytraVisualUpdatePayload read(RegistryFriendlyByteBuf buffer) {
		return new ArmouredElytraVisualUpdatePayload(buffer.readBoolean()
				? Optional.of(ArmouredElytraVisual.STREAM_CODEC.decode(buffer))
				: Optional.empty());
	}
}

