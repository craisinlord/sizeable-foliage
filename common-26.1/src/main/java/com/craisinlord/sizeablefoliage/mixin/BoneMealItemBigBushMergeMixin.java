package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
public abstract class BoneMealItemBigBushMergeMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$mergeBushes(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        if (!level.getBlockState(clickedPos).is(Blocks.BUSH)) {
            return;
        }

        Block bigBush = BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "big_bush"));
        if (!(bigBush instanceof BigBushBlock)) {
            return;
        }

        for (Direction forward : Direction.Plane.HORIZONTAL) {
            Direction side = forward.getClockWise();
            BlockPos origin = clickedPos;
            BlockPos[] columns = new BlockPos[]{
                    origin, origin.relative(forward), origin.relative(side), origin.relative(forward).relative(side)};

            boolean allBush = true;
            for (BlockPos column : columns) {
                if (!level.getBlockState(column).is(Blocks.BUSH)) {
                    allBush = false;
                    break;
                }
            }
            if (!allBush) {
                continue;
            }

            BlockState grown = bigBush.defaultBlockState()
                    .setValue(BigBushBlock.AGE, 1)
                    .setValue(BigBushBlock.FACING, forward);
            if (!BigBushBlock.hasFullSupport(grown, level, origin)) {
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

            if (!level.isClientSide()) {
                for (BlockPos column : columns) {
                    level.removeBlock(column, false);
                }
                BigBushBlock.placeBushBlocks(level, origin, grown, Block.UPDATE_ALL);
                if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                    ItemStack stack = context.getItemInHand();
                    stack.shrink(1);
                }
                level.levelEvent(1505, origin, 15);
            }

            cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            return;
        }
    }
}
