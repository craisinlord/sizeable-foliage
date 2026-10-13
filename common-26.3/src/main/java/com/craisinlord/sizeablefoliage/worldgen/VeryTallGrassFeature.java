package com.craisinlord.sizeablefoliage.worldgen;

import com.craisinlord.sizeablefoliage.block.VeryTallGrassBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

/** Places a single 3-tall {@link VeryTallGrassBlock} column, used for sparse natural generation. */
public record VeryTallGrassFeature() implements Feature {
    public static final MapCodec<VeryTallGrassFeature> CODEC = MapCodec.unit(VeryTallGrassFeature::new);

    @Override
    public MapCodec<VeryTallGrassFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos) {
        if (!level.getBlockState(pos).isAir()
                || !level.getBlockState(pos.above()).isAir()
                || !level.getBlockState(pos.above(2)).isAir()) {
            return false;
        }
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
            return false;
        }
        VeryTallGrassBlock.placeAt(level, pos, Block.UPDATE_ALL);
        return true;
    }
}
