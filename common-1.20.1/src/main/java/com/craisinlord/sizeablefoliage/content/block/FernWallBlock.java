package com.craisinlord.sizeablefoliage.content.block;

import com.craisinlord.sizeablefoliage.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FernWallBlock extends BushBlock implements BonemealableBlock {
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final EnumProperty<Connection> CONNECTION = EnumProperty.create("connection", Connection.class);
    public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");

    private static final VoxelShape COLLISION_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public FernWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, Part.LOWER)
                .setValue(CONNECTION, Connection.SINGLE)
                .setValue(ROTATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, CONNECTION, ROTATED);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return switch (state.getValue(PART)) {
            case LOWER -> super.canSurvive(state, level, pos);
            case MIDDLE -> level.getBlockState(pos.below()).is(this)
                    && level.getBlockState(pos.below()).getValue(PART) == Part.LOWER
                    && level.getBlockState(pos.above()).is(this)
                    && level.getBlockState(pos.above()).getValue(PART) == Part.UPPER;
            case UPPER -> level.getBlockState(pos.below()).is(this)
                    && level.getBlockState(pos.below()).getValue(PART) == Part.MIDDLE;
        };
    }

    private static boolean isFernWall(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof FernWallBlock;
    }

    private static BlockState withConnection(BlockState state, LevelReader level, BlockPos pos) {
        boolean west = isFernWall(level, pos.west());
        boolean east = isFernWall(level, pos.east());
        boolean north = isFernWall(level, pos.north());
        boolean south = isFernWall(level, pos.south());
        int count = (west ? 1 : 0) + (east ? 1 : 0) + (north ? 1 : 0) + (south ? 1 : 0);

        boolean rotated = !west && !east && (north || south);

        Connection connection;
        if (count >= 2) {
            connection = Connection.BOTH;
        } else if (west || north) {
            connection = Connection.LEFT;
        } else if (east || south) {
            connection = Connection.RIGHT;
        } else {
            connection = Connection.SINGLE;
        }
        return state.setValue(CONNECTION, connection).setValue(ROTATED, rotated);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        if (direction.getAxis().isHorizontal()) {
            state = withConnection(state, level, pos);
        }
        if (state.getValue(PART) != Part.LOWER) {
            return state;
        }
        if (direction == Direction.DOWN) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            for (BlockPos otherPos : otherPartPositions(state, pos)) {
                if (level.getBlockState(otherPos).is(this)) {
                    level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private static List<BlockPos> otherPartPositions(BlockState state, BlockPos pos) {
        return switch (state.getValue(PART)) {
            case LOWER -> List.of(pos.above(), pos.above(2));
            case MIDDLE -> List.of(pos.below(), pos.above());
            case UPPER -> List.of(pos.below(), pos.below(2));
        };
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (!level.getBlockState(pos.above()).canBeReplaced(context) || !level.getBlockState(pos.above(2)).canBeReplaced(context)) {
            return null;
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        placeAt(level, pos, Block.UPDATE_ALL);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this, 1));
        level.levelEvent(1505, pos, 15);
    }

    public static void placeAt(LevelAccessor level, BlockPos pos, int flags) {
        level.setBlock(pos, stateFor(level, pos, Part.LOWER), flags);
        level.setBlock(pos.above(), stateFor(level, pos.above(), Part.MIDDLE), flags);
        level.setBlock(pos.above(2), stateFor(level, pos.above(2), Part.UPPER), flags);
    }

    private static BlockState stateFor(LevelAccessor level, BlockPos pos, Part part) {
        LevelReader reader = (LevelReader) level;
        return withConnection(defaultState(part), reader, pos);
    }

    private static BlockState defaultState(Part part) {
        return net.minecraft.core.registries.BuiltInRegistries.BLOCK
                .get(new net.minecraft.resources.ResourceLocation(Constants.MOD_ID, "fern_wall"))
                .defaultBlockState()
                .setValue(PART, part);
    }

    public enum Part implements StringRepresentable {
        LOWER("lower"),
        MIDDLE("middle"),
        UPPER("upper");

        private final String name;

        Part(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    public enum Connection implements StringRepresentable {
        SINGLE("single"),
        LEFT("left"),
        RIGHT("right"),
        BOTH("both");

        private final String name;

        Connection(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
