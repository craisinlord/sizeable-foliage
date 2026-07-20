package com.craisinlord.sizeablefoliage.neoforge.registry;

import com.craisinlord.sizeablefoliage.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public final class NeoForgeCreativeModeTabs {
    // 26.1's CreativeModeTabs.NATURAL_BLOCKS field is private; rebuild the same vanilla registry key.
    private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("natural_blocks"));

    private NeoForgeCreativeModeTabs() {
    }

    @SubscribeEvent
    public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == NATURAL_BLOCKS) {
            event.accept(NeoForgeModBlocks.TORCHFLOWER_BUSH_ITEM.get());
            event.accept(NeoForgeModBlocks.VERY_SHORT_GRASS_ITEM.get());
            event.accept(NeoForgeModBlocks.VERY_TALL_GRASS_ITEM.get());
            event.accept(NeoForgeModBlocks.SWEET_BERRY_SEEDS_ITEM.get());
            event.accept(NeoForgeModBlocks.FERN_WALL_ITEM.get());
        }
    }
}
