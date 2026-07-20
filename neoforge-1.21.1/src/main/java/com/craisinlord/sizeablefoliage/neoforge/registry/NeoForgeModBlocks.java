package com.craisinlord.sizeablefoliage.neoforge.registry;

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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredBlock<BigBushBlock> BIG_BUSH = BLOCKS.register("big_bush", ModBlocks::createBigBush);
    public static final DeferredBlock<BigBushPartBlock> BIG_BUSH_PART = BLOCKS.register("big_bush_part", ModBlocks::createBigBushPart);
    public static final DeferredBlock<TorchflowerBushBlock> TORCHFLOWER_BUSH = BLOCKS.register("torchflower_bush", ModBlocks::createTorchflowerBush);
    public static final DeferredBlock<VeryShortGrassBlock> VERY_SHORT_GRASS = BLOCKS.register("very_short_grass", ModBlocks::createVeryShortGrass);
    public static final DeferredBlock<VeryTallGrassBlock> VERY_TALL_GRASS = BLOCKS.register("very_tall_grass", ModBlocks::createVeryTallGrass);
    public static final DeferredBlock<BigSweetBerryBushBlock> BIG_SWEET_BERRY_BUSH = BLOCKS.register("big_sweet_berry_bush", ModBlocks::createBigSweetBerryBush);
    public static final DeferredBlock<BigSweetBerryBushPartBlock> BIG_SWEET_BERRY_BUSH_PART = BLOCKS.register("big_sweet_berry_bush_part", ModBlocks::createBigSweetBerryBushPart);
    public static final DeferredBlock<FernWallBlock> FERN_WALL = BLOCKS.register("fern_wall", ModBlocks::createFernWall);

    public static final DeferredItem<BlockItem> BIG_BUSH_ITEM = ITEMS.register("big_bush",
            () -> new BigBushBlockItem(BIG_BUSH.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> TORCHFLOWER_BUSH_ITEM = ITEMS.registerSimpleBlockItem(TORCHFLOWER_BUSH);
    public static final DeferredItem<BlockItem> VERY_SHORT_GRASS_ITEM = ITEMS.registerSimpleBlockItem(VERY_SHORT_GRASS);
    public static final DeferredItem<BlockItem> VERY_TALL_GRASS_ITEM = ITEMS.registerSimpleBlockItem(VERY_TALL_GRASS);
    public static final DeferredItem<BlockItem> SWEET_BERRY_SEEDS_ITEM = ITEMS.register("sweet_berry_seeds",
            () -> new BlockItem(net.minecraft.world.level.block.Blocks.SWEET_BERRY_BUSH, new Item.Properties()));
    public static final DeferredItem<BlockItem> FERN_WALL_ITEM = ITEMS.registerSimpleBlockItem(FERN_WALL);

    private NeoForgeModBlocks() {
    }

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
