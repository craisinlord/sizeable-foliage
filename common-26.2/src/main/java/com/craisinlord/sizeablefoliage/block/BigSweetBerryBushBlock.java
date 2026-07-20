package com.craisinlord.sizeablefoliage.block;

import com.craisinlord.sizeablefoliage.Constants;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BigSweetBerryBushBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<BigSweetBerryBushBlock> CODEC = Block.simpleCodec(BigSweetBerryBushBlock::new);

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty BERRY_STAGE = IntegerProperty.create("berry_stage", 0, 2);
    public static final BooleanProperty SHEARED = BooleanProperty.create("sheared");

    public static final int MAX_BERRY_STAGE = 2;
    private static final double GROWTH_CHANCE = (1.0 / 5.0) / 2.5;

    private static final int MIN_BERRIES = 4;
    private static final int MAX_BERRIES = 7;

    public BigSweetBerryBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.EAST)
                .setValue(BERRY_STAGE, MAX_BERRY_STAGE)
                .setValue(SHEARED, false));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<BushBlock> codec() {
        return (MapCodec<BushBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BERRY_STAGE, SHEARED);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(seedsItem());
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        applyThorns(state, level, entity);
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    public static void applyThorns(BlockState state, Level level, Entity entity) {
        if (entity instanceof LivingEntity && !entity.is(EntityTypes.FOX) && !entity.is(EntityTypes.BEE)) {
            entity.makeStuckInBlock(state, new Vec3(0.8, 0.75, 0.8));
            if (level instanceof ServerLevel serverLevel) {
                Vec3 movement = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
                if (movement.horizontalDistanceSqr() > 0.0) {
                    double xs = Math.abs(movement.x());
                    double zs = Math.abs(movement.z());
                    if (xs >= 0.003F || zs >= 0.003F) {
                        entity.hurtServer(serverLevel, level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    static net.minecraft.world.item.Item seedsItem() {
        return BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "sweet_berry_seeds"));
    }

    public static List<BlockPos> partPositions(BlockState state, BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
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
        return positions;
    }

    public static List<BlockPos> supportColumns(BlockState state, BlockPos origin) {
        Direction forward = state.getValue(FACING);
        Direction side = forward.getClockWise();
        List<BlockPos> columns = new ArrayList<>();
        columns.add(origin);
        columns.add(origin.relative(forward));
        columns.add(origin.relative(side));
        columns.add(origin.relative(forward).relative(side));
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
        return BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "big_sweet_berry_bush_part"));
    }

    public static void placeBushBlocks(LevelAccessor level, BlockPos origin, BlockState grownState, int flags) {
        level.setBlock(origin, grownState, flags);
        Block part = partBlock();
        for (BlockPos pos : partPositions(grownState, origin)) {
            level.setBlock(pos, part.defaultBlockState(), flags);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        for (BlockPos partPos : partPositions(state, pos)) {
            if (level.getBlockState(partPos).getBlock() instanceof BigSweetBerryBushPartBlock) {
                level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(SHEARED) && state.getValue(BERRY_STAGE) < MAX_BERRY_STAGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(SHEARED) && state.getValue(BERRY_STAGE) < MAX_BERRY_STAGE && random.nextDouble() < GROWTH_CHANCE) {
            level.setBlock(pos, state.setValue(BERRY_STAGE, state.getValue(BERRY_STAGE) + 1), 2);
        }
    }

    private static void harvest(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
        if (!level.isClientSide()) {
            int count = MIN_BERRIES + level.getRandom().nextInt(MAX_BERRIES - MIN_BERRIES + 1);
            popResourceFromFace(level, pos, Direction.UP, new ItemStack(Items.SWEET_BERRIES, count));
            level.setBlock(pos, state.setValue(BERRY_STAGE, 0), 2);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(BERRY_STAGE) == MAX_BERRY_STAGE) {
            harvest(level, pos, state, player);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    public static boolean shear(Level level, BlockPos originPos, BlockState originState, Player player) {
        if (!(originState.getBlock() instanceof BigSweetBerryBushBlock) || originState.getValue(BERRY_STAGE) != MAX_BERRY_STAGE) {
            return false;
        }
        if (!level.isClientSide()) {
            int count = MIN_BERRIES + level.getRandom().nextInt(MAX_BERRIES - MIN_BERRIES + 1);
            popResourceFromFace(level, originPos, Direction.UP, new ItemStack(Items.SWEET_BERRIES, count));
            level.setBlock(originPos, originState.setValue(BERRY_STAGE, 0).setValue(SHEARED, true), 2);
            level.playSound(null, originPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F);
        }
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !state.getValue(SHEARED) && state.getValue(BERRY_STAGE) < MAX_BERRY_STAGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return !state.getValue(SHEARED) && state.getValue(BERRY_STAGE) < MAX_BERRY_STAGE;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (!state.getValue(SHEARED) && state.getValue(BERRY_STAGE) < MAX_BERRY_STAGE) {
            level.setBlock(pos, state.setValue(BERRY_STAGE, state.getValue(BERRY_STAGE) + 1), 2);
            level.levelEvent(1505, pos, 15);
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
        if (stack.is(Items.SHEARS) && shear(level, pos, state, player)) {
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        if (stack.is(Items.BONE_MEAL) && isValidBonemealTarget(level, pos, state)) {
            if (!level.isClientSide()) {
                performBonemeal((ServerLevel) level, level.getRandom(), pos, state);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
