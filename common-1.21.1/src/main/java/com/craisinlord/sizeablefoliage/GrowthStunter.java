package com.craisinlord.sizeablefoliage;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.List;

public final class GrowthStunter extends SavedData {
    private static final String ID = Constants.MOD_ID + "_growth_stunter";

    public static final SavedData.Factory<GrowthStunter> FACTORY = new SavedData.Factory<>(
            GrowthStunter::new,
            (tag, provider) -> load(tag),
            null
    );

    private final LongOpenHashSet stunted = new LongOpenHashSet();

    public static GrowthStunter get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, ID);
    }

    public boolean isStunted(BlockPos pos) {
        return stunted.contains(pos.asLong());
    }

    public void setStunted(BlockPos pos) {
        if (stunted.add(pos.asLong())) {
            setDirty();
        }
    }

    private static GrowthStunter load(net.minecraft.nbt.CompoundTag tag) {
        GrowthStunter stunter = new GrowthStunter();
        for (long value : tag.getLongArray("stunted")) {
            stunter.stunted.add(value);
        }
        return stunter;
    }

    @Override
    public net.minecraft.nbt.CompoundTag save(net.minecraft.nbt.CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
        tag.putLongArray("stunted", List.copyOf(stunted));
        return tag;
    }
}
