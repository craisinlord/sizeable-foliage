package com.craisinlord.sizeablefoliage.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemSweetBerriesMixin {
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$blockSweetBerriesPlanting(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getItemInHand().is(Items.SWEET_BERRIES)) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
