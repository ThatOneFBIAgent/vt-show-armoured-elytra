package iza.armourelytra.network;

import java.util.Optional;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ArmouredElytraVisualSyncPayload(int entityId, Optional<ArmouredElytraVisual> visual) implements CustomPacketPayload {
	public static final Type<ArmouredElytraVisualSyncPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("vt-show-armoured-elytra", "sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ArmouredElytraVisualSyncPayload> CODEC = StreamCodec.ofMember(
			ArmouredElytraVisualSyncPayload::write,
			ArmouredElytraVisualSyncPayload::read);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	private void write(RegistryFriendlyByteBuf buffer) {
		ByteBufCodecs.VAR_INT.encode(buffer, entityId);
		buffer.writeBoolean(visual.isPresent());
		visual.ifPresent(value -> ArmouredElytraVisual.STREAM_CODEC.encode(buffer, value));
	}

	private static ArmouredElytraVisualSyncPayload read(RegistryFriendlyByteBuf buffer) {
		int entityId = ByteBufCodecs.VAR_INT.decode(buffer);
		return new ArmouredElytraVisualSyncPayload(entityId, buffer.readBoolean()
				? Optional.of(ArmouredElytraVisual.STREAM_CODEC.decode(buffer))
				: Optional.empty());
	}
}

