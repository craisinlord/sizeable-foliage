package com.craisinlord.sizeablefoliage.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
    public static final MapCodec<BigSweetBerryBushPartBlock> CODEC = Block.simpleCodec(BigSweetBerryBushPartBlock::new);

    public BigSweetBerryBushPartBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<BigSweetBerryBushPartBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        BigSweetBerryBushBlock.applyThorns(state, level, entity);
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    @Nullable
    public static BlockPos findOrigin(LevelReader level, BlockPos partPos) {
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
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BlockPos origin = findOrigin(level, pos);
            if (origin != null) {
                level.destroyBlock(origin, false);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        BlockPos origin = findOrigin(level, pos);
        if (origin != null) {
            level.destroyBlock(origin, true);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess tickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random
    ) {
        tickAccess.scheduleTick(pos, this, 1);
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
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
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockPos origin = findOrigin(level, pos);
        return origin != null && level.getBlockState(origin).getBlock() instanceof BigSweetBerryBushBlock bush
                && bush.isValidBonemealTarget(level, origin, level.getBlockState(origin));
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
    protected InteractionResult useItemOn(
            ItemStack stack,
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
            if (stack.is(Items.SHEARS) && BigSweetBerryBushBlock.shear(level, origin, originState, player)) {
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
            if (stack.is(Items.BONE_MEAL) && isValidBonemealTarget(level, pos, state) && !level.isClientSide()) {
                performBonemeal((ServerLevel) level, level.getRandom(), pos, state);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockPos origin = findOrigin(level, pos);
        if (origin != null) {
            BlockState originState = level.getBlockState(origin);
            if (originState.getBlock() instanceof BigSweetBerryBushBlock) {
                return originState.useWithoutItem(level, player, hitResult.withPosition(origin));
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(BigSweetBerryBushBlock.seedsItem());
    }
}
