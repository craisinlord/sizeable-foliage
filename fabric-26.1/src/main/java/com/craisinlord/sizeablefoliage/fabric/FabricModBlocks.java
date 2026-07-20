package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.block.BigSweetBerryBushBlock;
import com.craisinlord.sizeablefoliage.block.BigSweetBerryBushPartBlock;
import com.craisinlord.sizeablefoliage.block.FernWallBlock;
import com.craisinlord.sizeablefoliage.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.block.VeryTallGrassBlock;
import com.craisinlord.sizeablefoliage.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

/** Fabric-native registration for the 4 Sizeable Foliage blocks (no DeferredRegister needed on Fabric). */
public final class FabricModBlocks {
    // 26.1's CreativeModeTabs.NATURAL_BLOCKS field is private; rebuild the same vanilla registry key.
    private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("natural_blocks"));

    public static final BigBushBlock BIG_BUSH = registerBlock("big_bush", ModBlocks::createBigBush);
    public static final BigBushPartBlock BIG_BUSH_PART = registerBlock("big_bush_part", ModBlocks::createBigBushPart);
    public static final TorchflowerBushBlock TORCHFLOWER_BUSH = registerBlock("torchflower_bush", ModBlocks::createTorchflowerBush);
    public static final VeryShortGrassBlock VERY_SHORT_GRASS = registerBlock("very_short_grass", ModBlocks::createVeryShortGrass);
    public static final VeryTallGrassBlock VERY_TALL_GRASS = registerBlock("very_tall_grass", ModBlocks::createVeryTallGrass);
    public static final BigSweetBerryBushBlock BIG_SWEET_BERRY_BUSH = registerBlock("big_sweet_berry_bush", ModBlocks::createBigSweetBerryBush);
    public static final BigSweetBerryBushPartBlock BIG_SWEET_BERRY_BUSH_PART = registerBlock("big_sweet_berry_bush_part", ModBlocks::createBigSweetBerryBushPart);
    public static final FernWallBlock FERN_WALL = registerBlock("fern_wall", ModBlocks::createFernWall);

    public static final BlockItem TORCHFLOWER_BUSH_ITEM = registerSimpleBlockItem("torchflower_bush", TORCHFLOWER_BUSH);
    public static final BlockItem VERY_SHORT_GRASS_ITEM = registerSimpleBlockItem("very_short_grass", VERY_SHORT_GRASS);
    public static final BlockItem VERY_TALL_GRASS_ITEM = registerSimpleBlockItem("very_tall_grass", VERY_TALL_GRASS);
    public static final BlockItem SWEET_BERRY_SEEDS_ITEM = registerItem("sweet_berry_seeds",
            key -> new BlockItem(net.minecraft.world.level.block.Blocks.SWEET_BERRY_BUSH, new Item.Properties().setId(key)));
    public static final BlockItem FERN_WALL_ITEM = registerSimpleBlockItem("fern_wall", FERN_WALL);

    private FabricModBlocks() {
    }

    public static void init() {
        net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents.modifyOutputEvent(NATURAL_BLOCKS).register(output -> {
            output.accept(TORCHFLOWER_BUSH_ITEM);
            output.accept(VERY_SHORT_GRASS_ITEM);
            output.accept(VERY_TALL_GRASS_ITEM);
            output.accept(SWEET_BERRY_SEEDS_ITEM);
            output.accept(FERN_WALL_ITEM);
        });
    }

    private static <T extends Block> T registerBlock(String path, Function<ResourceKey<Block>, T> factory) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(key));
    }

    private static BlockItem registerSimpleBlockItem(String path, Block block) {
        return registerItem(path, key -> new BlockItem(block, new Item.Properties().setId(key)));
    }

    private static BlockItem registerItem(String path, Function<ResourceKey<net.minecraft.world.item.Item>, BlockItem> factory) {
        ResourceKey<net.minecraft.world.item.Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(key));
    }
}
