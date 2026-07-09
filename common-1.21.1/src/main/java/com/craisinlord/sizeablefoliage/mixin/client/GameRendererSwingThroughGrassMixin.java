package com.craisinlord.sizeablefoliage.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererSwingThroughGrassMixin {
    @Shadow
    private Minecraft minecraft;

    @Shadow
    private static HitResult filterHitResult(HitResult hitResult, Vec3 eyePosition, double range) {
        throw new AssertionError();
    }

    @Unique
    private Double sizeableFoliage$swingThroughReachSq;

    @Inject(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/HitResult;getLocation()Lnet/minecraft/world/phys/Vec3;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void sizeableFoliage$cacheSwingThroughReach(
            Entity entity,
            double blockInteractionRange,
            double entityInteractionRange,
            float partialTick,
            CallbackInfoReturnable<HitResult> cir,
            double maxRange,
            double maxRangeSq,
            Vec3 eyePosition,
            HitResult hitResult
    ) {
        this.sizeableFoliage$swingThroughReachSq = null;

        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        ClientLevel level = this.minecraft.level;
        if (level == null) {
            return;
        }

        BlockPos blockPos = blockHitResult.getBlockPos();
        if (level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty()) {
            this.sizeableFoliage$swingThroughReachSq = maxRangeSq;
        }
    }

    @ModifyArg(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;"
            ),
            index = 5
    )
    private double sizeableFoliage$useOriginalReachForEntityRaycast(double rangeSq) {
        return this.sizeableFoliage$swingThroughReachSq == null ? rangeSq : this.sizeableFoliage$swingThroughReachSq;
    }

    @Inject(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/EntityHitResult;getLocation()Lnet/minecraft/world/phys/Vec3;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void sizeableFoliage$preferAttackableEntityBehindGrass(
            Entity entity,
            double blockInteractionRange,
            double entityInteractionRange,
            float partialTick,
            CallbackInfoReturnable<HitResult> cir,
            double maxRange,
            double maxRangeSq,
            Vec3 eyePosition,
            HitResult blockHitResult,
            double blockHitDistanceSq,
            Vec3 viewVector,
            Vec3 reachEnd,
            float unused,
            AABB aabb,
            EntityHitResult entityHitResult
    ) {
        if (this.sizeableFoliage$swingThroughReachSq == null || entityHitResult == null) {
            return;
        }

        Entity hitEntity = entityHitResult.getEntity();
        if (!(hitEntity instanceof LivingEntity)
                || hitEntity.isSpectator()
                || !hitEntity.isAttackable()
                || this.minecraft.player == null
                || hitEntity == this.minecraft.player.getVehicle()) {
            return;
        }

        if (eyePosition.distanceToSqr(hitEntity.position()) < this.sizeableFoliage$swingThroughReachSq) {
            cir.setReturnValue(filterHitResult(entityHitResult, eyePosition, entityInteractionRange));
        }
    }
}
