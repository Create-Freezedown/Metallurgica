package dev.metallurgists.metallurgica.compat.kubejs.helpers;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;

public class FluidIngredientHelper {
    public static FluidIngredient toFluidIngredient(FluidStackJS fluidStack) {
        return FluidIngredient.fromFluidStack(FluidStackHooksForge.toForge(fluidStack.getFluidStack()));
    }

    // Commented out because it is unused and needs reformatting

    //public static FlowSource.FluidHandler createEffectHandler(FluidIngredient fluidIngredient, BiConsumer<OpenEndedPipe, FluidStackJS> handler) {
    //    return new OpenEndedPipe.FluidHandler() {
    //        @Override
    //        public boolean canApplyEffects(OpenEndedPipe pipe, FluidStack fluid) {
    //            return fluidIngredient.test(fluid);
    //        }
//
    //        @Override
    //        public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
    //            handler.accept(pipe, FluidStackJS.of(fluid));
    //        }
    //    };
    //}

//    public static BlockSpoutingBehaviour createSpoutingHandler(BlockStatePredicate block, SpecialSpoutHandlerEvent.SpoutHandler handler) {
//        return new BlockSpoutingBehaviour() {
//            @Override
//            public int fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
//                if (!block.test(world.getBlockState(pos))) {
//                    return 0;
//                }
//                return (int) handler.fillBlock(new BlockContainerJS(world, pos), FluidStackJS.of(FluidStackHooksForge.fromForge(availableFluid)), simulate);
//            }
//        };
//    }
}