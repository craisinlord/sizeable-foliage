package com.craisinlord.sizeablefoliage;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;

/** Registers the mod's items into vanilla's composter, same mechanism vanilla leaves/saplings use. */
public final class ModCompostables {
    private ModCompostables() {
    }

    public static void register(Item bigBush, Item torchflowerBush, Item veryShortGrass, Item veryTallGrass) {
        ComposterBlock.COMPOSTABLES.put(bigBush, 0.5F);
        ComposterBlock.COMPOSTABLES.put(torchflowerBush, 0.65F);
        ComposterBlock.COMPOSTABLES.put(veryShortGrass, 0.3F);
        ComposterBlock.COMPOSTABLES.put(veryTallGrass, 0.3F);
    }
}
