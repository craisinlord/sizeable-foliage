package com.craisinlord.sizeablefoliage.forge.client;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.forge.registry.ForgeModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ForgeModClient {
    private ForgeModClient() {
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) -> level != null && pos != null
                        ? BiomeColors.getAverageGrassColor(level, pos)
                        : -1,
                ForgeModBlocks.BIG_BUSH.get(),
                ForgeModBlocks.BIG_BUSH_PART.get(),
                ForgeModBlocks.VERY_SHORT_GRASS.get(),
                ForgeModBlocks.VERY_TALL_GRASS.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> FoliageColor.getDefaultColor(),
                ForgeModBlocks.BIG_BUSH_ITEM.get(),
                ForgeModBlocks.VERY_SHORT_GRASS_ITEM.get(),
                ForgeModBlocks.VERY_TALL_GRASS_ITEM.get()
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ForgeModBlocks.BIG_BUSH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ForgeModBlocks.BIG_BUSH_PART.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ForgeModBlocks.TORCHFLOWER_BUSH.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ForgeModBlocks.VERY_SHORT_GRASS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ForgeModBlocks.VERY_TALL_GRASS.get(), RenderType.cutout());
        });
    }
}
