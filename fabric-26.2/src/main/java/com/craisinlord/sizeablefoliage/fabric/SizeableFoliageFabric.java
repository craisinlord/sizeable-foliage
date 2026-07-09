package com.craisinlord.sizeablefoliage.fabric;

import net.fabricmc.api.ModInitializer;
import com.craisinlord.sizeablefoliage.SizeableFoliage;

public class SizeableFoliageFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SizeableFoliage.init();
        FabricModBlocks.init();
        FabricModFeatures.init();
        FabricModWorldgen.init();
    }
}
