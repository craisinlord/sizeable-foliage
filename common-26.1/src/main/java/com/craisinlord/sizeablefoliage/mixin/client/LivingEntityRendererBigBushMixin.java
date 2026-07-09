package com.craisinlord.sizeablefoliage.mixin.client;

import com.craisinlord.sizeablefoliage.client.BigBushRenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 26.x split entity rendering into extractRenderState (has the live entity) and submit
 * (only has the detached render state), so the per-entity translucency factor computed
 * during extraction is bridged to submit via a weakly-keyed map instead of a field.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererBigBushMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Unique
    private static final Map<LivingEntityRenderState, Float> sizeableFoliage$translucencyByState = new WeakHashMap<>();

    @Unique
    private float sizeableFoliage$bushTranslucency = 1.0F;

    @Shadow
    public abstract Identifier getTextureLocation(S state);

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void sizeableFoliage$extractBigBush(T entity, S state, float partialTicks, CallbackInfo ci) {
        sizeableFoliage$translucencyByState.put(state, BigBushRenderHelper.translucencyFactor(entity));
    }

    @Inject(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void sizeableFoliage$captureBigBush(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        this.sizeableFoliage$bushTranslucency = sizeableFoliage$translucencyByState.getOrDefault(state, 1.0F);
    }

    @Inject(
            method = "getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/rendertype/RenderType;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sizeableFoliage$bigBushRenderType(S state, boolean isBodyVisible, boolean forceTransparent, boolean appearGlowing, CallbackInfoReturnable<RenderType> cir) {
        if (isBodyVisible && !forceTransparent && !appearGlowing && this.sizeableFoliage$bushTranslucency < 1.0F) {
            cir.setReturnValue(RenderTypes.entityTranslucentCullItemTarget(this.getTextureLocation(state)));
        }
    }

    @Inject(
            method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sizeableFoliage$hideBigBushNameTag(T entity, double distanceToCameraSq, CallbackInfoReturnable<Boolean> cir) {
        if (BigBushRenderHelper.isInsideBigBush(entity)) {
            cir.setReturnValue(false);
        }
    }

    @ModifyArg(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
            ),
            index = 6
    )
    private int sizeableFoliage$bigBushAlpha(int tintedColor) {
        if (tintedColor != -1 || this.sizeableFoliage$bushTranslucency >= 1.0F) {
            return tintedColor;
        }
        int alpha = Math.round(this.sizeableFoliage$bushTranslucency * 255.0F);
        return (alpha << 24) | 0xFFFFFF;
    }
}
