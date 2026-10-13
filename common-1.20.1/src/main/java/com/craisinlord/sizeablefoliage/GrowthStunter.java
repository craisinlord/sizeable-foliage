package com.craisinlord.sizeablefoliage;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class GrowthStunter extends SavedData {
    private static final String ID = Constants.MOD_ID + "_growth_stunter";

    private final LongOpenHashSet stunted = new LongOpenHashSet();

    public static GrowthStunter get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(GrowthStunter::load, GrowthStunter::new, ID);
    }

    public boolean isStunted(BlockPos pos) {
        return stunted.contains(pos.asLong());
    }

    public void setStunted(BlockPos pos) {
        if (stunted.add(pos.asLong())) {
            setDirty();
        }
    }

    private static GrowthStunter load(CompoundTag tag) {
        GrowthStunter stunter = new GrowthStunter();
        for (long value : tag.getLongArray("stunted")) {
            stunter.stunted.add(value);
        }
        return stunter;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("stunted", new LongArrayTag(stunted.longStream().toArray()));
        return tag;
    }
}
