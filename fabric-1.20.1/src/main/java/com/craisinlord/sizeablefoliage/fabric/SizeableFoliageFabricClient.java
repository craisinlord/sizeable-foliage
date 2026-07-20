package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.content.client.BigBushCameraHider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;

public class SizeableFoliageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(
                (state, level, pos, tintIndex) -> level != null && pos != null
                        ? BiomeColors.getAverageGrassColor(level, pos)
                        : -1,
                FabricModBlocks.BIG_BUSH,
                FabricModBlocks.BIG_BUSH_PART,
                FabricModBlocks.VERY_SHORT_GRASS,
                FabricModBlocks.VERY_TALL_GRASS,
                FabricModBlocks.FERN_WALL
        );
        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> GrassColor.getDefaultColor(),
                FabricModBlocks.BIG_BUSH_ITEM,
                FabricModBlocks.VERY_SHORT_GRASS_ITEM,
                FabricModBlocks.VERY_TALL_GRASS_ITEM,
                FabricModBlocks.FERN_WALL_ITEM
        );

        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.BIG_BUSH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.BIG_BUSH_PART, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.TORCHFLOWER_BUSH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.VERY_SHORT_GRASS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.VERY_TALL_GRASS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.FERN_WALL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.BIG_SWEET_BERRY_BUSH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricModBlocks.BIG_SWEET_BERRY_BUSH_PART, RenderType.cutout());

        ClientTickEvents.END_CLIENT_TICK.register(BigBushCameraHider::update);
    }
}
