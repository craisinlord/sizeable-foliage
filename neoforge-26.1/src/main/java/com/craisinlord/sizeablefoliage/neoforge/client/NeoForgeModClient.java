package com.craisinlord.sizeablefoliage.neoforge.client;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.neoforge.registry.NeoForgeModBlocks;
import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

/**
 * Grass tint via the data-driven BlockTintSource system (26.1 replaced the old
 * imperative RegisterColorHandlersEvent.Block/.Item). Item-icon tint and cutout
 * render-type registration are not ported for 26.1 yet -- the client rendering
 * pipeline was substantially rewritten (submit/extractRenderState split, RenderType
 * moved packages, ItemBlockRenderTypes no longer exists) and needs more
 * investigation to port those cosmetic pieces correctly.
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeModClient {
    private NeoForgeModClient() {
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(
                List.of(BlockTintSources.grass()),
                NeoForgeModBlocks.BIG_BUSH.get(),
                NeoForgeModBlocks.BIG_BUSH_PART.get(),
                NeoForgeModBlocks.VERY_SHORT_GRASS.get(),
                NeoForgeModBlocks.VERY_TALL_GRASS.get(),
                NeoForgeModBlocks.FERN_WALL.get()
        );
    }
}
