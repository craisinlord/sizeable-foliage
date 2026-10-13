package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.GrowthStunter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SaplingBlock.class)
public abstract class SaplingGrowthStuntMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$skipStuntedSapling(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (GrowthStunter.get(level).isStunted(pos)) {
            ci.cancel();
        }
    }

    @Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$blockStuntedBonemeal(LevelReader level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof ServerLevel serverLevel && GrowthStunter.get(serverLevel).isStunted(pos)) {
            cir.setReturnValue(false);
        }
    }
}
