package com.craisinlord.sizeablefoliage.forge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.TorchflowerBushBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryTallGrassBlock;
import com.craisinlord.sizeablefoliage.content.item.BigBushBlockItem;
import com.craisinlord.sizeablefoliage.registry.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeModBlocks {
    private static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    private static final DeferredRegister<net.minecraft.world.item.Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<BigBushBlock> BIG_BUSH = BLOCKS.register("big_bush", ModBlocks::createBigBush);
    public static final RegistryObject<BigBushPartBlock> BIG_BUSH_PART = BLOCKS.register("big_bush_part", ModBlocks::createBigBushPart);
    public static final RegistryObject<TorchflowerBushBlock> TORCHFLOWER_BUSH = BLOCKS.register("torchflower_bush", ModBlocks::createTorchflowerBush);
    public static final RegistryObject<VeryShortGrassBlock> VERY_SHORT_GRASS = BLOCKS.register("very_short_grass", ModBlocks::createVeryShortGrass);
    public static final RegistryObject<VeryTallGrassBlock> VERY_TALL_GRASS = BLOCKS.register("very_tall_grass", ModBlocks::createVeryTallGrass);

    public static final RegistryObject<BlockItem> BIG_BUSH_ITEM = ITEMS.register("big_bush",
            () -> new BigBushBlockItem(BIG_BUSH.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> TORCHFLOWER_BUSH_ITEM = ITEMS.register("torchflower_bush",
            () -> new BlockItem(TORCHFLOWER_BUSH.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> VERY_SHORT_GRASS_ITEM = ITEMS.register("very_short_grass",
            () -> new BlockItem(VERY_SHORT_GRASS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> VERY_TALL_GRASS_ITEM = ITEMS.register("very_tall_grass",
            () -> new BlockItem(VERY_TALL_GRASS.get(), new Item.Properties()));

    private ForgeModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
