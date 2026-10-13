package com.craisinlord.sizeablefoliage;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.List;

public final class GrowthStunter extends SavedData {
    private static final Codec<GrowthStunter> CODEC = Codec.LONG.listOf().xmap(
            list -> {
                GrowthStunter stunter = new GrowthStunter();
                stunter.stunted.addAll(list);
                return stunter;
            },
            stunter -> List.copyOf(stunter.stunted)
    );

    public static final SavedDataType<GrowthStunter> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "growth_stunter"),
            GrowthStunter::new,
            CODEC,
            null
    );

    private final LongOpenHashSet stunted = new LongOpenHashSet();

    public static GrowthStunter get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean isStunted(BlockPos pos) {
        return stunted.contains(pos.asLong());
    }

    public void setStunted(BlockPos pos) {
        if (stunted.add(pos.asLong())) {
            setDirty();
        }
    }
}
