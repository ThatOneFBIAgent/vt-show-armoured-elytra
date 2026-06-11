package iza.armourelytra.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import iza.armourelytra.client.RenderHelper;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HumanoidArmorLayer.class)
public abstract class ArmouredElytraArmorLayerMixin {
	@ModifyVariable(method = "renderArmorPiece", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private ItemStack vtShowArmouredElytra$useBundledChestplate(ItemStack stack, PoseStack poseStack, SubmitNodeCollector collector, ItemStack originalStack, EquipmentSlot slot, int light, HumanoidRenderState state) {
		return RenderHelper.getChestplateForRender(stack, state);
	}
}
