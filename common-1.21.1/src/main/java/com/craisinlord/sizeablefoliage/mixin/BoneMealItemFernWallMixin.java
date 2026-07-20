package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.block.FernWallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemFernWallMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$growFernWall(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(pos);
        if (!clickedState.is(Blocks.LARGE_FERN)) {
            return;
        }

        BlockPos basePos = clickedState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        BlockState baseState = level.getBlockState(basePos);
        BlockState topState = level.getBlockState(basePos.above());
        if (!baseState.is(Blocks.LARGE_FERN)
                || baseState.getValue(DoublePlantBlock.HALF) != DoubleBlockHalf.LOWER
                || !topState.is(Blocks.LARGE_FERN)
                || topState.getValue(DoublePlantBlock.HALF) != DoubleBlockHalf.UPPER) {
            return;
        }

        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fern_wall"));
        if (!(block instanceof FernWallBlock)) {
            return;
        }

        BlockState fernWallState = block.defaultBlockState();
        if (!level.getBlockState(basePos.above(2)).canBeReplaced()
                || !fernWallState.canSurvive(level, basePos)) {
            return;
        }

        if (!level.isClientSide) {
            FernWallBlock.placeAt(level, basePos, Block.UPDATE_ALL);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                ItemStack stack = context.getItemInHand();
                stack.shrink(1);
            }
            level.levelEvent(1505, basePos, 15);
        }

        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
    }
}
