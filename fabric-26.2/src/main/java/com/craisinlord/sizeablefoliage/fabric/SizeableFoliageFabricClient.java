package com.craisinlord.sizeablefoliage.fabric;

import com.craisinlord.sizeablefoliage.client.BigBushCameraHider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSources;

import java.util.List;

public class SizeableFoliageFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Grass tint via the data-driven BlockTintSource system (26.1 replaced the old
        // imperative ColorProviderRegistry). Item-icon tint and cutout render-type
        // registration are not ported for 26.1 yet -- the client rendering pipeline was
        // substantially rewritten (submit/extractRenderState split, RenderType moved
        // packages, ItemBlockRenderTypes/BlockRenderLayerMap no longer exist) and needs
        // more investigation to port those cosmetic pieces correctly.
        BlockColorRegistry.register(
                List.of(BlockTintSources.grass()),
                FabricModBlocks.BIG_BUSH,
                FabricModBlocks.BIG_BUSH_PART,
                FabricModBlocks.VERY_SHORT_GRASS,
                FabricModBlocks.VERY_TALL_GRASS
        );

        ClientTickEvents.END_CLIENT_TICK.register(BigBushCameraHider::update);
    }
}
