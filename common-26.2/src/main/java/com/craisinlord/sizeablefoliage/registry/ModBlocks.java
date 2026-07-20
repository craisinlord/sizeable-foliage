package com.craisinlord.sizeablefoliage.registry;

import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.block.BigSweetBerryBushBlock;
import com.craisinlord.sizeablefoliage.block.BigSweetBerryBushPartBlock;
import com.craisinlord.sizeablefoliage.block.FernWallBlock;
import com.craisinlord.sizeablefoliage.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.block.VeryTallGrassBlock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    private ModBlocks() {
    }

    public static BigBushBlock createBigBush(ResourceKey<Block> key) {
        return new BigBushBlock(BlockBehaviour.Properties.of()
                .setId(key)
                .mapColor(MapColor.PLANT)
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
    }

    public static BigBushPartBlock createBigBushPart(ResourceKey<Block> key) {
        return new BigBushPartBlock(BlockBehaviour.Properties.of()
                .setId(key)
                .mapColor(MapColor.PLANT)
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .noLootTable());
    }

    public static TorchflowerBushBlock createTorchflowerBush(ResourceKey<Block> key) {
        return new TorchflowerBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PEONY)
                .setId(key)
                .lightLevel(state -> 15));
    }

    public static VeryShortGrassBlock createVeryShortGrass(ResourceKey<Block> key) {
        return new VeryShortGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)
                .setId(key));
    }

    public static VeryTallGrassBlock createVeryTallGrass(ResourceKey<Block> key) {
        return new VeryTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)
                .setId(key));
    }

    public static BigSweetBerryBushBlock createBigSweetBerryBush(ResourceKey<Block> key) {
        return new BigSweetBerryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
                .setId(key)
                .noOcclusion());
    }

    public static BigSweetBerryBushPartBlock createBigSweetBerryBushPart(ResourceKey<Block> key) {
        return new BigSweetBerryBushPartBlock(BlockBehaviour.Properties.of()
                .setId(key)
                .mapColor(MapColor.PLANT)
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable());
    }

    public static FernWallBlock createFernWall(ResourceKey<Block> key) {
        return new FernWallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_FERN)
                .setId(key)
                .offsetType(BlockBehaviour.OffsetType.NONE));
    }
}
