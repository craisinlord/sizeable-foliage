package com.craisinlord.sizeablefoliage.registry;

import com.craisinlord.sizeablefoliage.content.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.FernWallBlock;
import com.craisinlord.sizeablefoliage.content.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryTallGrassBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Loader-agnostic block property definitions, shared by the Fabric and NeoForge
 * registration classes so the properties stay in sync across loaders.
 */
public final class ModBlocks {
    private ModBlocks() {
    }

    public static BigBushBlock createBigBush() {
        return new BigBushBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY));
    }

    public static BigBushPartBlock createBigBushPart() {
        return new BigBushPartBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .noLootTable());
    }

    public static TorchflowerBushBlock createTorchflowerBush() {
        return new TorchflowerBushBlock(BlockBehaviour.Properties.copy(Blocks.PEONY)
                .lightLevel(state -> 15));
    }

    public static VeryShortGrassBlock createVeryShortGrass() {
        return new VeryShortGrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS));
    }

    public static VeryTallGrassBlock createVeryTallGrass() {
        return new VeryTallGrassBlock(BlockBehaviour.Properties.copy(Blocks.TALL_GRASS));
    }

    public static BigSweetBerryBushBlock createBigSweetBerryBush() {
        return new BigSweetBerryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)
                .noOcclusion());
    }

    public static BigSweetBerryBushPartBlock createBigSweetBerryBushPart() {
        return new BigSweetBerryBushPartBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable());
    }

    public static FernWallBlock createFernWall() {
        return new FernWallBlock(BlockBehaviour.Properties.copy(Blocks.LARGE_FERN)
                .offsetType(BlockBehaviour.OffsetType.NONE));
    }
}
