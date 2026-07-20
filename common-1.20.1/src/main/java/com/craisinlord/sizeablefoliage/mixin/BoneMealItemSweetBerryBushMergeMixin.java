package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemSweetBerryBushMergeMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$mergeSweetBerryBushes(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);
        if (!clickedState.is(Blocks.SWEET_BERRY_BUSH)) {
            return;
        }

        Block bigBush = BuiltInRegistries.BLOCK.get(new ResourceLocation(Constants.MOD_ID, "big_sweet_berry_bush"));
        if (!(bigBush instanceof BigSweetBerryBushBlock)) {
            return;
        }

        for (Direction forward : Direction.Plane.HORIZONTAL) {
            Direction side = forward.getClockWise();
            BlockPos origin = clickedPos;
            BlockPos[] columns = new BlockPos[]{
                    origin, origin.relative(forward), origin.relative(side), origin.relative(forward).relative(side)};

            boolean allBushes = true;
            for (BlockPos column : columns) {
                if (!level.getBlockState(column).is(Blocks.SWEET_BERRY_BUSH)) {
                    allBushes = false;
                    break;
                }
            }
            if (!allBushes) {
                continue;
            }

            BlockState grown = bigBush.defaultBlockState()
                    .setValue(BigSweetBerryBushBlock.FACING, forward)
                    .setValue(BigSweetBerryBushBlock.BERRY_STAGE, 0);
            if (!BigSweetBerryBushBlock.hasFullSupport(grown, level, origin)) {
                continue;
            }
            boolean roomAbove = true;
            for (BlockPos column : columns) {
                if (!level.getBlockState(column.above()).canBeReplaced()) {
                    roomAbove = false;
                    break;
                }
            }
            if (!roomAbove) {
                continue;
            }

            if (!level.isClientSide) {
                for (BlockPos column : columns) {
                    level.removeBlock(column, false);
                }
                BigSweetBerryBushBlock.placeBushBlocks(level, origin, grown, Block.UPDATE_ALL);
                if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                    ItemStack stack = context.getItemInHand();
                    stack.shrink(1);
                }
                level.levelEvent(1505, origin, 15);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }
    }
}
