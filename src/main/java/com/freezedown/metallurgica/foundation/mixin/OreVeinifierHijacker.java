package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.content.world.VeinifierHijackerTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(OreVeinifier.class)
public class OreVeinifierHijacker {
    /**
     * @author PouffyDev
     * @reason Hijack the OreVeinifier to use our own Deposit Blocks
     */
    @Overwrite
    protected static NoiseChunk.BlockStateFiller create(DensityFunction densityFunction, DensityFunction densityFunction1, DensityFunction densityFunction2, PositionalRandomFactory positionalRandomFactory) {
        BlockState blockstate = null;
        return (pos) -> {
            double d0 = densityFunction.compute(pos);
            int i = pos.blockY();
            VeinifierHijackerTypes oreveinifier$veintype = d0 > 0.0D ? VeinifierHijackerTypes.COPPER : VeinifierHijackerTypes.IRON;
            double d1 = Math.abs(d0);
            int j = oreveinifier$veintype.maxY - i;
            int k = i - oreveinifier$veintype.minY;
            if (k >= 0 && j >= 0) {
                int l = Math.min(j, k);
                double d2 = Mth.clampedMap(l, 0.0D, 20.0D, -0.2D, 0.0D);
                if (d1 + d2 < (double)0.4F) {
                    return blockstate;
                } else {
                    RandomSource randomsource = positionalRandomFactory.at(pos.blockX(), i, pos.blockZ());
                    if (randomsource.nextFloat() > 0.7F) {
                        return blockstate;
                    } else if (densityFunction1.compute(pos) >= 0.0D) {
                        return blockstate;
                    } else {
                        double d3 = Mth.clampedMap(d1, 0.4F, 0.6F, 0.1F, 0.3F);
                        if ((double)randomsource.nextFloat() < d3 && densityFunction2.compute(pos) > (double)-0.3F) {
                            return randomsource.nextFloat() < 0.02F ? oreveinifier$veintype.rawOreBlock : oreveinifier$veintype.ore;
                        } else {
                            return oreveinifier$veintype.filler;
                        }
                    }
                }
            } else {
                return blockstate;
            }
        };
    }
    
}
