package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.FernWallBlock;
import com.craisinlord.sizeablefoliage.content.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryTallGrassBlock;
import com.craisinlord.sizeablefoliage.content.item.BigBushBlockItem;
import com.craisinlord.sizeablefoliage.registry.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;

/** Fabric-native registration for the 4 Sizeable Foliage blocks (no DeferredRegister needed on Fabric). */
public final class FabricModBlocks {
    public static final BigBushBlock BIG_BUSH = register("big_bush", ModBlocks.createBigBush());
    public static final BigBushPartBlock BIG_BUSH_PART = register("big_bush_part", ModBlocks.createBigBushPart());
    public static final TorchflowerBushBlock TORCHFLOWER_BUSH = register("torchflower_bush", ModBlocks.createTorchflowerBush());
    public static final VeryShortGrassBlock VERY_SHORT_GRASS = register("very_short_grass", ModBlocks.createVeryShortGrass());
    public static final VeryTallGrassBlock VERY_TALL_GRASS = register("very_tall_grass", ModBlocks.createVeryTallGrass());
    public static final BigSweetBerryBushBlock BIG_SWEET_BERRY_BUSH = register("big_sweet_berry_bush", ModBlocks.createBigSweetBerryBush());
    public static final BigSweetBerryBushPartBlock BIG_SWEET_BERRY_BUSH_PART = register("big_sweet_berry_bush_part", ModBlocks.createBigSweetBerryBushPart());
    public static final FernWallBlock FERN_WALL = register("fern_wall", ModBlocks.createFernWall());

    public static final BlockItem BIG_BUSH_ITEM = registerItem("big_bush",
            new BigBushBlockItem(BIG_BUSH, new Item.Properties()));
    public static final BlockItem TORCHFLOWER_BUSH_ITEM = registerSimpleBlockItem("torchflower_bush", TORCHFLOWER_BUSH);
    public static final BlockItem VERY_SHORT_GRASS_ITEM = registerSimpleBlockItem("very_short_grass", VERY_SHORT_GRASS);
    public static final BlockItem VERY_TALL_GRASS_ITEM = registerSimpleBlockItem("very_tall_grass", VERY_TALL_GRASS);
    public static final BlockItem SWEET_BERRY_SEEDS_ITEM = registerItem("sweet_berry_seeds",
            new BlockItem(net.minecraft.world.level.block.Blocks.SWEET_BERRY_BUSH, new Item.Properties()));
    public static final BlockItem FERN_WALL_ITEM = registerSimpleBlockItem("fern_wall", FERN_WALL);

    private FabricModBlocks() {
    }

    public static void init() {
        net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.accept(BIG_BUSH_ITEM);
            entries.accept(TORCHFLOWER_BUSH_ITEM);
            entries.accept(VERY_SHORT_GRASS_ITEM);
            entries.accept(VERY_TALL_GRASS_ITEM);
            entries.accept(SWEET_BERRY_SEEDS_ITEM);
            entries.accept(FERN_WALL_ITEM);
        });
    }

    private static <T extends Block> T register(String path, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path), block);
    }

    private static BlockItem registerSimpleBlockItem(String path, Block block) {
        return registerItem(path, new BlockItem(block, new Item.Properties()));
    }

    private static BlockItem registerItem(String path, BlockItem item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path), item);
    }
}
