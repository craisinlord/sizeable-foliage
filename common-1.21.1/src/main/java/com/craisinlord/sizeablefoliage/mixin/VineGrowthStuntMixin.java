package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.GrowthStunter;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VineBlock.class)
public abstract class VineGrowthStuntMixin {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$skipStuntedVine(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (GrowthStunter.get(level).isStunted(pos)) {
            ci.cancel();
        }
    }
}
