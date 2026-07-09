package com.craisinlord.sizeablefoliage.mixin.client;

import com.craisinlord.sizeablefoliage.content.client.BigBushRenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererBigBushMixin {
    @Unique
    private float sizeableFoliage$bushTranslucency = 1.0F;

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
    )
    private void sizeableFoliage$checkBigBush(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.sizeableFoliage$bushTranslucency = BigBushRenderHelper.translucencyFactor(entity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$bigBushRenderType(LivingEntity entity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        if (bodyVisible && !translucent && !glowing && BigBushRenderHelper.isInsideBigBush(entity)) {
            cir.setReturnValue(RenderType.itemEntityTranslucentCull(((EntityRenderer) (Object) this).getTextureLocation(entity)));
        }
    }

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$hideBigBushNameTag(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (BigBushRenderHelper.isInsideBigBush(entity)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"),
            index = 4
    )
    private int sizeableFoliage$bigBushAlpha(int color) {
        if (color != -1 || this.sizeableFoliage$bushTranslucency >= 1.0F) {
            return color;
        }
        int alpha = Math.round(this.sizeableFoliage$bushTranslucency * 255.0F);
        return (alpha << 24) | 0xFFFFFF;
    }
}
