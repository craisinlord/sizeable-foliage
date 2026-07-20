package com.craisinlord.sizeablefoliage.forge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ForgeCreativeModeTabs {
    private ForgeCreativeModeTabs() {
    }

    @SubscribeEvent
    public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ForgeModBlocks.TORCHFLOWER_BUSH_ITEM.get());
            event.accept(ForgeModBlocks.BIG_BUSH_ITEM.get());
            event.accept(ForgeModBlocks.VERY_SHORT_GRASS_ITEM.get());
            event.accept(ForgeModBlocks.VERY_TALL_GRASS_ITEM.get());
            event.accept(ForgeModBlocks.SWEET_BERRY_SEEDS_ITEM.get());
            event.accept(ForgeModBlocks.FERN_WALL_ITEM.get());
        }
    }
}
