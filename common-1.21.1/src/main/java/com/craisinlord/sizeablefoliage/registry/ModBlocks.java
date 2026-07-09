package com.craisinlord.sizeablefoliage.registry;

import com.craisinlord.sizeablefoliage.content.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigBushPartBlock;
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
        return new TorchflowerBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PEONY)
                .lightLevel(state -> 15));
    }

    public static VeryShortGrassBlock createVeryShortGrass() {
        return new VeryShortGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS));
    }

    public static VeryTallGrassBlock createVeryTallGrass() {
        return new VeryTallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS));
    }
}
