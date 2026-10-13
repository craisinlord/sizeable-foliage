package com.craisinlord.sizeablefoliage.worldgen;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.block.BigSweetBerryBushBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public record BigSweetBerryBushWorldgenFeature() implements Feature {
    public static final MapCodec<BigSweetBerryBushWorldgenFeature> CODEC = MapCodec.unit(BigSweetBerryBushWorldgenFeature::new);

    @Override
    public MapCodec<BigSweetBerryBushWorldgenFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {

        Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "big_sweet_berry_bush"));
        if (!(block instanceof BigSweetBerryBushBlock)) {
            return false;
        }

        List<Direction> facings = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        for (int i = facings.size() - 1; i > 0; i--) {
            Direction swap = facings.set(random.nextInt(i + 1), facings.get(i));
            facings.set(i, swap);
        }

        for (Direction facing : facings) {
            BlockState grown = block.defaultBlockState()
                    .setValue(BigSweetBerryBushBlock.FACING, facing)
                    .setValue(BigSweetBerryBushBlock.BERRY_STAGE, BigSweetBerryBushBlock.MAX_BERRY_STAGE);
            if (canPlace(level, origin, grown)) {
                BigSweetBerryBushBlock.placeBushBlocks(level, origin, grown, Block.UPDATE_ALL);
                return true;
            }
        }
        return false;
    }

    private boolean canPlace(WorldGenLevel level, BlockPos origin, BlockState grown) {
        if (!BigSweetBerryBushBlock.hasFullSupport(grown, level, origin)) {
            return false;
        }
        List<BlockPos> positions = new ArrayList<>(BigSweetBerryBushBlock.partPositions(grown, origin));
        positions.add(origin);
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            if (!state.canBeReplaced() || !state.getFluidState().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
