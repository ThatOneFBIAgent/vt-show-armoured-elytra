package iza.armourelytra.client.mixin;

import iza.armourelytra.client.RenderHelper;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WingsLayer.class)
public abstract class ArmouredElytraWingsLayerMixin {
	@ModifyVariable(method = "submit", at = @At("STORE"), ordinal = 0)
	private ItemStack vtShowArmouredElytra$useBundledElytra(ItemStack stack) {
		return RenderHelper.getBundledElytra(stack);
	}
}
