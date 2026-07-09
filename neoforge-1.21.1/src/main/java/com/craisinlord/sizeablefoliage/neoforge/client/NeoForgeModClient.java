package com.craisinlord.sizeablefoliage.neoforge.client;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class NeoForgeModClient {
    private NeoForgeModClient() {
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) -> level != null && pos != null
                        ? BiomeColors.getAverageGrassColor(level, pos)
                        : -1,
                NeoForgeModBlocks.BIG_BUSH.get(),
                NeoForgeModBlocks.BIG_BUSH_PART.get(),
                NeoForgeModBlocks.VERY_SHORT_GRASS.get(),
                NeoForgeModBlocks.VERY_TALL_GRASS.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> GrassColor.getDefaultColor(),
                NeoForgeModBlocks.BIG_BUSH_ITEM.get(),
                NeoForgeModBlocks.VERY_SHORT_GRASS_ITEM.get(),
                NeoForgeModBlocks.VERY_TALL_GRASS_ITEM.get()
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(NeoForgeModBlocks.BIG_BUSH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(NeoForgeModBlocks.BIG_BUSH_PART.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(NeoForgeModBlocks.TORCHFLOWER_BUSH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(NeoForgeModBlocks.VERY_SHORT_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(NeoForgeModBlocks.VERY_TALL_GRASS.get(), RenderType.cutout());
        });
    }
}
