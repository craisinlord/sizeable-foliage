package com.craisinlord.sizeablefoliage.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneNoWaterMixin {
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$noWaterToPlant(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is((net.minecraft.world.level.block.Block) (Object) this)) {
            return;
        }
        cir.setReturnValue(belowState.is(BlockTags.SUPPORTS_SUGAR_CANE));
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$requireWaterToGrow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!sizeableFoliage$hasAdjacentWater(level, pos)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean sizeableFoliage$hasAdjacentWater(LevelReader level, BlockPos pos) {
        BlockPos basePos = pos;
        while (level.getBlockState(basePos.below()).is(net.minecraft.world.level.block.Blocks.SUGAR_CANE)) {
            basePos = basePos.below();
        }
        BlockPos below = basePos.below();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighbor = below.relative(direction);
            if (level.getFluidState(neighbor).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
}
