package com.craisinlord.sizeablefoliage.registry;

import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.block.BigBushPartBlock;
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

/**
 * Loader-agnostic block property definitions, shared by the Fabric and NeoForge
 * registration classes so the properties stay in sync across loaders.
 *
 * 26.1 requires {@code Properties.setId(key)} to be set before a block is constructed
 * (blocks now resolve their own registry id during {@code BlockBehaviour}'s constructor),
 * so every factory method here takes the block's own {@link ResourceKey}.
 */
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
}
