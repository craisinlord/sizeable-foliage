package com.craisinlord.sizeablefoliage.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BigSweetBerryBushPartBlock extends Block implements BonemealableBlock {
    public BigSweetBerryBushPartBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BigSweetBerryBushBlock.applyThorns(state, level, entity);
        super.entityInside(state, level, pos, entity);
    }

    @Nullable
    public static BlockPos findOrigin(BlockGetter level, BlockPos partPos) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 0; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }
                    BlockPos candidate = partPos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(candidate);
                    if (state.getBlock() instanceof BigSweetBerryBushBlock
                            && BigSweetBerryBushBlock.partPositions(state, candidate).contains(partPos)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos origin = findOrigin(level, pos);
            if (origin != null) {
                level.destroyBlock(origin, false);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            BlockPos origin = findOrigin(level, pos);
            if (origin != null) {
                level.destroyBlock(origin, true);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        level.scheduleTick(pos, this, 1);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos origin = findOrigin(level, pos);
        if (origin == null) {
            level.removeBlock(pos, false);
            return;
        }
        BlockState originState = level.getBlockState(origin);
        if (!originState.canSurvive(level, origin)) {
            level.destroyBlock(origin, true);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        BlockPos origin = findOrigin(level, pos);
        return origin != null && level.getBlockState(origin).getBlock() instanceof BigSweetBerryBushBlock bush
                && bush.isValidBonemealTarget(level, origin, level.getBlockState(origin), isClient);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos origin = findOrigin(level, pos);
        return origin != null && level.getBlockState(origin).getBlock() instanceof BigSweetBerryBushBlock bush
                && bush.isBonemealSuccess(level, random, origin, level.getBlockState(origin));
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos origin = findOrigin(level, pos);
        if (origin != null && level.getBlockState(origin).getBlock() instanceof BigSweetBerryBushBlock bush) {
            bush.performBonemeal(level, random, origin, level.getBlockState(origin));
        }
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        BlockPos origin = findOrigin(level, pos);
        if (origin != null && level.getBlockState(origin).getBlock() instanceof BigSweetBerryBushBlock) {
            BlockState originState = level.getBlockState(origin);
            return originState.use(level, player, hand, hitResult.withPosition(origin));
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(BigSweetBerryBushBlock.seedsItem());
    }
}
