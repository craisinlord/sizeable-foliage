package com.craisinlord.sizeablefoliage.neoforge.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import com.craisinlord.sizeablefoliage.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeCreativeModeTabs {
    private NeoForgeCreativeModeTabs() {
    }

    @SubscribeEvent
    public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(NeoForgeModBlocks.TORCHFLOWER_BUSH_ITEM.get());
            event.accept(NeoForgeModBlocks.BIG_BUSH_ITEM.get());
            event.accept(NeoForgeModBlocks.VERY_SHORT_GRASS_ITEM.get());
            event.accept(NeoForgeModBlocks.VERY_TALL_GRASS_ITEM.get());
            event.accept(NeoForgeModBlocks.SWEET_BERRY_SEEDS_ITEM.get());
            event.accept(NeoForgeModBlocks.FERN_WALL_ITEM.get());
        }
    }
}
