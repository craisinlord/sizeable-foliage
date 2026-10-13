package com.craisinlord.sizeablefoliage.worldgen;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

/** Places a {@link BigBushBlock} at a fixed growth stage (age), used for sparse overworld generation. */
public record BigBushWorldgenFeature(int age) implements Feature {
    public static final MapCodec<BigBushWorldgenFeature> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(Codec.intRange(1, 2).fieldOf("age").forGetter(BigBushWorldgenFeature::age)).apply(i, BigBushWorldgenFeature::new));

    @Override
    public MapCodec<BigBushWorldgenFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {

        Block block = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "big_bush"));
        if (!(block instanceof BigBushBlock)) {
            return false;
        }

        List<Direction> facings = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        for (int i = facings.size() - 1; i > 0; i--) {
            Direction swap = facings.set(random.nextInt(i + 1), facings.get(i));
            facings.set(i, swap);
        }

        for (Direction facing : facings) {
            BlockState grown = block.defaultBlockState().setValue(BigBushBlock.AGE, age).setValue(BigBushBlock.FACING, facing);
            if (canPlace(level, origin, grown)) {
                BigBushBlock.placeBushBlocks(level, origin, grown, Block.UPDATE_ALL);
                return true;
            }
            if (age == 0) {
                // Facing is irrelevant at age 0 (single block); no need to try every rotation.
                break;
            }
        }
        return false;
    }

    private boolean canPlace(WorldGenLevel level, BlockPos origin, BlockState grown) {
        if (!BigBushBlock.hasFullSupport(grown, level, origin)) {
            return false;
        }
        List<BlockPos> positions = new ArrayList<>(BigBushBlock.partPositions(grown, origin));
        positions.add(origin);
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            // canBeReplaced() alone isn't enough: water is replaceable, so a footprint column
            // sitting in a puddle would silently pass and get overwritten by bush foliage.
            if (!state.canBeReplaced() || !state.getFluidState().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
