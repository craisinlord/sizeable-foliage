package com.craisinlord.sizeablefoliage.content.client;

import com.craisinlord.sizeablefoliage.content.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigBushPartBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushBlock;
import com.craisinlord.sizeablefoliage.content.block.BigSweetBerryBushPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class BigBushRenderHelper {
    private BigBushRenderHelper() {
    }

    public static boolean isInsideBigBush(LivingEntity entity) {
        return translucencyFactor(entity) < 1.0F;
    }

    /** 1.0 = fully opaque (outside any bush), fading down to 0.0 (fully invisible) at the bush's center. */
    public static float translucencyFactor(LivingEntity entity) {
        Level level = entity.level();
        BushVolume volume = bushVolumeAt(level, entity.blockPosition());
        if (volume == null) {
            volume = bushVolumeAt(level, BlockPos.containing(entity.getEyePosition()));
        }
        if (volume == null) {
            return 1.0F;
        }
        double distance = entity.getBoundingBox().getCenter().distanceTo(volume.center());
        return (float) Mth.clamp(distance / volume.radius(), 0.0, 1.0);
    }

    private record BushVolume(Vec3 center, double radius) {}

    @Nullable
    private static BushVolume bushVolumeAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockPos origin;
        BlockState originState;
        if (state.getBlock() instanceof BigBushPartBlock) {
            origin = BigBushPartBlock.findOrigin(level, pos);
            if (origin == null) {
                return null;
            }
            originState = level.getBlockState(origin);
        } else if (state.getBlock() instanceof BigBushBlock && state.getValue(BigBushBlock.AGE) > 0) {
            origin = pos;
            originState = state;
        } else if (state.getBlock() instanceof BigSweetBerryBushPartBlock) {
            origin = BigSweetBerryBushPartBlock.findOrigin(level, pos);
            if (origin == null) {
                return null;
            }
            originState = level.getBlockState(origin);
            return twoByTwoVolume(origin, originState.getValue(BigSweetBerryBushBlock.FACING));
        } else if (state.getBlock() instanceof BigSweetBerryBushBlock) {
            return twoByTwoVolume(pos, state.getValue(BigSweetBerryBushBlock.FACING));
        } else {
            return null;
        }
        int age = originState.getValue(BigBushBlock.AGE);
        if (age == 2) {
            Vec3 center = new Vec3(origin.getX() + 0.5, origin.getY() + 1.5, origin.getZ() + 0.5);
            return new BushVolume(center, 1.5);
        }
        if (age == 1) {
            return twoByTwoVolume(origin, originState.getValue(BigBushBlock.FACING));
        }
        return null;
    }

    private static BushVolume twoByTwoVolume(BlockPos origin, Direction forward) {
        Direction side = forward.getClockWise();
        Vec3 center = new Vec3(
                origin.getX() + 0.5 + 0.5 * forward.getStepX() + 0.5 * side.getStepX(),
                origin.getY() + 1.0,
                origin.getZ() + 0.5 + 0.5 * forward.getStepZ() + 0.5 * side.getStepZ()
        );
        return new BushVolume(center, 1.0);
    }
}
