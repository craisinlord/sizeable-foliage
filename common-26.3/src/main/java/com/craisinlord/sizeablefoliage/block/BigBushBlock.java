package com.craisinlord.sizeablefoliage.block;

import com.craisinlord.sizeablefoliage.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BigBushBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 1, 2);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BigBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 1)
                .setValue(FACING, Direction.EAST));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(Blocks.BUSH);
    }

    public static List<BlockPos> partPositions(BlockState state, BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        int age = state.getValue(AGE);
        if (age == 1) {
            Direction forward = state.getValue(FACING);
            Direction side = forward.getClockWise();
            for (int dy = 0; dy <= 1; dy++) {
                for (BlockPos column : new BlockPos[]{
                        origin, origin.relative(forward), origin.relative(side), origin.relative(forward).relative(side)}) {
                    BlockPos pos = column.above(dy);
                    if (!pos.equals(origin)) {
                        positions.add(pos);
                    }
                }
            }
        } else if (age == 2) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }
                        positions.add(origin.offset(dx, dy, dz));
                    }
                }
            }
        }
        return positions;
    }

    public static List<BlockPos> supportColumns(BlockState state, BlockPos origin) {
        List<BlockPos> columns = new ArrayList<>();
        int age = state.getValue(AGE);
        if (age == 1) {
            Direction forward = state.getValue(FACING);
            Direction side = forward.getClockWise();
            columns.add(origin);
            columns.add(origin.relative(forward));
            columns.add(origin.relative(side));
            columns.add(origin.relative(forward).relative(side));
        } else if (age == 2) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    columns.add(origin.offset(dx, 0, dz));
                }
            }
        } else {
            columns.add(origin);
        }
        return columns;
    }

    public static boolean hasFullSupport(BlockState state, LevelReader level, BlockPos origin) {
        for (BlockPos column : supportColumns(state, origin)) {
            BlockPos below = column.below();
            if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return hasFullSupport(state, level, pos);
    }

    private static Block partBlock() {
        return BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "big_bush_part"));
    }

    public static void placeBushBlocks(LevelAccessor level, BlockPos origin, BlockState grownState, int flags) {
        level.setBlock(origin, grownState, flags);
        Block part = partBlock();
        BlockPos capPos = grownState.getValue(AGE) == 2 ? origin.above(2) : null;
        for (BlockPos pos : partPositions(grownState, origin)) {
            level.setBlock(pos, part.defaultBlockState()
                    .setValue(BigBushPartBlock.CAP, pos.equals(capPos)), flags);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        for (BlockPos partPos : partPositions(state, pos)) {
            if (level.getBlockState(partPos).getBlock() instanceof BigBushPartBlock) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    /** Big Bush no longer grows via its own bone meal target - growth only happens by merging a grid of small bushes. */
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, BonemealSource source) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, BonemealSource source) {
    }

    /** Shears shrink a bush by one stage: 3x3 to 2x2, or 2x2 back down to a single vanilla Bush. */
    public static boolean shear(Level level, BlockPos originPos, BlockState originState, Player player, ItemStack shears, InteractionHand hand) {
        if (!(originState.getBlock() instanceof BigBushBlock) || level.isClientSide()) {
            return false;
        }
        int age = originState.getValue(AGE);
        if (age == 2) {
            BlockState shrunk = originState.setValue(AGE, 1);
            Set<BlockPos> keep = new HashSet<>(partPositions(shrunk, originPos));
            keep.add(originPos);
            for (BlockPos partPos : partPositions(originState, originPos)) {
                if (!keep.contains(partPos)) {
                    level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
                }
            }
            placeBushBlocks(level, originPos, shrunk, 3);
            level.levelEvent(1505, originPos, 15);
            shears.hurtAndBreak(1, player, hand);
            return true;
        }
        if (age == 1) {
            for (BlockPos partPos : partPositions(originState, originPos)) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
            }
            level.setBlock(originPos, Blocks.BUSH.defaultBlockState(), 3);
            level.levelEvent(1505, originPos, 15);
            shears.hurtAndBreak(1, player, hand);
            return true;
        }
        return false;
    }

    public static boolean grantBonemealBonus(Level level, BlockPos originPos, BlockState originState, Player player, ItemStack stack) {
        if (!(originState.getBlock() instanceof BigBushBlock bigBush)) {
            return false;
        }
        if (!level.isClientSide()) {
            bigBush.popResource(level, originPos, new ItemStack(Blocks.BUSH, 1));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.levelEvent(1505, originPos, 15);
        }
        return true;
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
        if (stack.is(Items.SHEARS) && shear(level, pos, state, player, stack, hand)) {
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        if (stack.is(Items.BONE_MEAL) && grantBonemealBonus(level, pos, state, player, stack)) {
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
