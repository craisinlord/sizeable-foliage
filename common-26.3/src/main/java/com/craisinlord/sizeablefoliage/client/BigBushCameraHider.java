package com.craisinlord.sizeablefoliage.client;

import com.craisinlord.sizeablefoliage.block.BigBushBlock;
import com.craisinlord.sizeablefoliage.block.BigBushPartBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public final class BigBushCameraHider {
    private static BlockPos hiddenOrigin;

    private BigBushCameraHider() {
    }

    public static void update(Minecraft mc) {
        BlockPos newOrigin = null;
        if (mc.level != null && mc.player != null) {
            Vec3 cameraPos = mc.gameRenderer.mainCamera().position();
            BlockPos pos = BlockPos.containing(cameraPos);
            BlockState state = mc.level.getBlockState(pos);
            if (state.getBlock() instanceof BigBushBlock && state.getValue(BigBushBlock.AGE) > 0) {
                newOrigin = pos;
            } else if (state.getBlock() instanceof BigBushPartBlock) {
                newOrigin = BigBushPartBlock.findOrigin(mc.level, pos);
            }
        }
        if (!Objects.equals(newOrigin, hiddenOrigin)) {
            BlockPos previous = hiddenOrigin;
            hiddenOrigin = newOrigin;
            markDirty(mc, previous);
            markDirty(mc, newOrigin);
        }
    }

    private static void markDirty(Minecraft mc, BlockPos origin) {
        if (origin != null && mc.level instanceof ClientLevel clientLevel) {
            clientLevel.setSectionRangeDirty(
                    SectionPos.blockToSectionCoord(origin.getX() - 2),
                    SectionPos.blockToSectionCoord(origin.getY() - 1),
                    SectionPos.blockToSectionCoord(origin.getZ() - 2),
                    SectionPos.blockToSectionCoord(origin.getX() + 2),
                    SectionPos.blockToSectionCoord(origin.getY() + 3),
                    SectionPos.blockToSectionCoord(origin.getZ() + 2));
        }
    }

    public static boolean shouldFade(BlockPos pos) {
        BlockPos origin = hiddenOrigin;
        if (origin == null) {
            return false;
        }
        return Math.abs(pos.getX() - origin.getX()) <= 1
                && Math.abs(pos.getZ() - origin.getZ()) <= 1
                && pos.getY() >= origin.getY()
                && pos.getY() <= origin.getY() + 2;
    }
}
