package com.craisinlord.sizeablefoliage.mixin;

import com.craisinlord.sizeablefoliage.Constants;
import com.craisinlord.sizeablefoliage.GrowthStunter;
import com.craisinlord.sizeablefoliage.content.block.VeryShortGrassBlock;
import com.craisinlord.sizeablefoliage.content.block.VeryTallGrassBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class ShearsPlantMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void sizeableFoliage$shearPlant(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.SHEARS) || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        Block block = state.getBlock();

        if (block instanceof VineBlock || block instanceof SaplingBlock) {
            GrowthStunter.get(serverLevel).setStunted(pos);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SHEEP_SHEAR, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }

        if (state.is(Blocks.GRASS)) {
            level.setBlock(pos, veryShortGrass(), Block.UPDATE_ALL);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SHEEP_SHEAR, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }

        if (state.is(Blocks.TALL_GRASS) && block instanceof DoublePlantBlock) {
            BlockPos basePos = state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
            level.removeBlock(basePos, false);
            level.removeBlock(basePos.above(), false);
            level.setBlock(basePos, Blocks.GRASS.defaultBlockState(), Block.UPDATE_ALL);
            level.playSound(null, basePos, net.minecraft.sounds.SoundEvents.SHEEP_SHEAR, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            return;
        }

        if (block instanceof VeryTallGrassBlock) {
            VeryTallGrassBlock.Part part = state.getValue(VeryTallGrassBlock.PART);
            BlockPos basePos = switch (part) {
                case LOWER -> pos;
                case MIDDLE -> pos.below();
                case UPPER -> pos.below(2);
            };
            level.removeBlock(basePos, false);
            level.removeBlock(basePos.above(), false);
            level.removeBlock(basePos.above(2), false);
            DoublePlantBlock.placeAt(level, Blocks.TALL_GRASS.defaultBlockState(), basePos, Block.UPDATE_ALL);
            level.playSound(null, basePos, net.minecraft.sounds.SoundEvents.SHEEP_SHEAR, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }

    @Unique
    private static BlockState veryShortGrass() {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation(Constants.MOD_ID, "very_short_grass")).defaultBlockState();
    }
}
