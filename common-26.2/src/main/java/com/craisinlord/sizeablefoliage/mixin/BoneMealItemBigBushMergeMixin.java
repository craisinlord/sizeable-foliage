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
import org.spongepowered.asm.mixin.Unique;
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

        if (sizeableFoliage$tryMerge3x3(context, level, clickedPos, bigBush, cir)) {
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

        if (!level.isClientSide()) {
            Block.popResource(level, clickedPos, new ItemStack(Blocks.BUSH, 1));
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            level.levelEvent(1505, clickedPos, 15);
        }
        cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
    }

    /** 3x3 grid of bushes merges into the bigger (3x3) Big Bush variant; tried before the 2x2 merge. */
    @Unique
    private boolean sizeableFoliage$tryMerge3x3(UseOnContext context, Level level, BlockPos clickedPos, Block bigBush, CallbackInfoReturnable<InteractionResult> cir) {
        for (int ox = -1; ox <= 1; ox++) {
            for (int oz = -1; oz <= 1; oz++) {
                BlockPos origin = clickedPos.offset(-ox, 0, -oz);
                BlockPos[] footprint = new BlockPos[9];
                int i = 0;
                boolean allBushes = true;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos p = origin.offset(dx, 0, dz);
                        footprint[i++] = p;
                        if (!level.getBlockState(p).is(Blocks.BUSH)) {
                            allBushes = false;
                        }
                    }
                }
                if (!allBushes) {
                    continue;
                }

                BlockState grown = bigBush.defaultBlockState().setValue(BigBushBlock.AGE, 2);
                if (!BigBushBlock.hasFullSupport(grown, level, origin)) {
                    continue;
                }
                boolean roomAbove = true;
                for (BlockPos p : footprint) {
                    if (!level.getBlockState(p.above()).canBeReplaced() || !level.getBlockState(p.above(2)).canBeReplaced()) {
                        roomAbove = false;
                        break;
                    }
                }
                if (!roomAbove) {
                    continue;
                }

                if (!level.isClientSide()) {
                    for (BlockPos p : footprint) {
                        level.removeBlock(p, false);
                    }
                    BigBushBlock.placeBushBlocks(level, origin, grown, Block.UPDATE_ALL);
                    if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                        context.getItemInHand().shrink(1);
                    }
                    level.levelEvent(1505, origin, 15);
                }
                cir.setReturnValue(level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
                return true;
            }
        }
        return false;
    }
}
