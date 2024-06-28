package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.FlowingGas;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasBlock;
import com.simibubi.create.content.fluids.pipes.VanillaFluidTargets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = VanillaFluidTargets.class, remap = false)
public class MixinVanillaFluidTargets {
    @Inject(method = "drainBlock", at = @At("TAIL"), cancellable = true)
    private static void drainBlock(Level level, BlockPos pos, BlockState state, boolean simulate, CallbackInfoReturnable<FluidStack> cir) {
        if (state.getBlock() instanceof GasBlock) {
            if (!simulate)
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            cir.setReturnValue(new FluidStack(
                    ((ForgeFlowingFluid) state.getFluidState().getType()).getSource(),
                    1000 * state.getFluidState().getAmount() / FlowingGas.MAX_DENSITY
            ));
            return;
        }
        
        Set<BlockPos> validBlocks = new HashSet<>();
        Set<BlockPos> testBlocks = new HashSet<>();
        
        BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1)).forEach(
                blockPos -> testBlocks.add(blockPos.offset(0, 0, 0))
        );
        
        for (BlockPos blockPos : testBlocks) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof GasBlock) {
                if (!simulate)
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                cir.setReturnValue(new FluidStack(
                        ((ForgeFlowingFluid) blockState.getFluidState().getType()).getSource(),
                        1000 * blockState.getFluidState().getAmount() / FlowingGas.MAX_DENSITY
                ));
                return;
            }
            if (blockState.isAir()) {
                Set<BlockPos> addingBlocks = new HashSet<>();
                BlockPos.betweenClosed(blockPos.offset(-1, 0, -1), blockPos.offset(1, 0, 1)).forEach(
                        pos1 -> addingBlocks.add(pos1.offset(0, 0, 0))
                );
                validBlocks.addAll(addingBlocks);
            }
        }
        
        for (BlockPos blockPos : validBlocks) {
            BlockState blockState = level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof GasBlock) {
                if (!simulate)
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                cir.setReturnValue(new FluidStack(
                        ((ForgeFlowingFluid) blockState.getFluidState().getType()).getSource(),
                        1000 * blockState.getFluidState().getAmount() / FlowingGas.MAX_DENSITY
                ));
                return;
            }
        }
    }
}
