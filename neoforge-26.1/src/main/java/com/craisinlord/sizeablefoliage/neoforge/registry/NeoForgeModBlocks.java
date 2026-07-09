package com.craisinlord.sizeablefoliage.neoforge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.block.VeryTallGrassBlock;
import com.craisinlord.sizeablefoliage.item.BigBushBlockItem;
import com.craisinlord.sizeablefoliage.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredBlock<BigBushBlock> BIG_BUSH = BLOCKS.register("big_bush",
            id -> ModBlocks.createBigBush(ResourceKey.create(Registries.BLOCK, id)));
    public static final DeferredBlock<BigBushPartBlock> BIG_BUSH_PART = BLOCKS.register("big_bush_part",
            id -> ModBlocks.createBigBushPart(ResourceKey.create(Registries.BLOCK, id)));
    public static final DeferredBlock<TorchflowerBushBlock> TORCHFLOWER_BUSH = BLOCKS.register("torchflower_bush",
            id -> ModBlocks.createTorchflowerBush(ResourceKey.create(Registries.BLOCK, id)));
    public static final DeferredBlock<VeryShortGrassBlock> VERY_SHORT_GRASS = BLOCKS.register("very_short_grass",
            id -> ModBlocks.createVeryShortGrass(ResourceKey.create(Registries.BLOCK, id)));
    public static final DeferredBlock<VeryTallGrassBlock> VERY_TALL_GRASS = BLOCKS.register("very_tall_grass",
            id -> ModBlocks.createVeryTallGrass(ResourceKey.create(Registries.BLOCK, id)));

    public static final DeferredItem<BlockItem> BIG_BUSH_ITEM = ITEMS.register("big_bush",
            id -> new BigBushBlockItem(BIG_BUSH.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<BlockItem> TORCHFLOWER_BUSH_ITEM = ITEMS.register("torchflower_bush",
            id -> new BlockItem(TORCHFLOWER_BUSH.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<BlockItem> VERY_SHORT_GRASS_ITEM = ITEMS.register("very_short_grass",
            id -> new BlockItem(VERY_SHORT_GRASS.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<BlockItem> VERY_TALL_GRASS_ITEM = ITEMS.register("very_tall_grass",
            id -> new BlockItem(VERY_TALL_GRASS.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    private NeoForgeModBlocks() {
    }

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
