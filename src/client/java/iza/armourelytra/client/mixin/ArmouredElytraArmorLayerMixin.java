package iza.armourelytra.client.mixin;

import iza.armourelytra.client.RenderHelper;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmouredElytraArmorLayerMixin {
	@ModifyVariable(method = "renderArmorPiece", at = @At("HEAD"), ordinal = 0)
	private ItemStack vtShowArmouredElytra$useBundledChestplate(ItemStack stack) {
		return RenderHelper.getBundledChestplate(stack);
	}
}
