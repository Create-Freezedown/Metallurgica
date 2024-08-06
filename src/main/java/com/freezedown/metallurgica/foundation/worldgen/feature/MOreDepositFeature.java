package com.freezedown.metallurgica.foundation.worldgen.feature;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.MOreDepositConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class MOreDepositFeature extends Feature<MOreDepositConfiguration> {
    
    public MOreDepositFeature() {
        super(MOreDepositConfiguration.CODEC);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<MOreDepositConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();
        MOreDepositConfiguration config = context.config();
        
        boolean large = randomsource.nextInt(5) == 0;
        int tipMin = (int) ((large ? 25 : 10) * 0.6);
        int tipRand = (int) ((large ? 35 : 20) * 0.3);
        int radiusMin = large ? 5 : 3;
        int radiusRand = large ? 3 : 1;
        
        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int topX = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        int topZ = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        
        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 to = new Vec3(pos.getX() + topX, pos.getY() + tip, pos.getZ() + topZ);
        
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    
                    if(worldgenlevel.getBlockState(posFromVec(from).below()).isAir()) {
                        continue;
                    }
                    
                    Vec3 per = to.subtract(from).normalize();
                    Vec3 current = from.add(0, 0, 0);
                    double distance = from.distanceTo(to);
                    
                    for (double i = 0; i < distance; i++) {
                        BlockPos targetPos = posFromVec(current);
                        if (i > 0 && i < distance / 1.3) {
                            int roll = randomsource.nextInt(3);
                            if (roll == 0) {
                                worldgenlevel.setBlock(targetPos, config.getSecondaryStone(), UPDATE_ALL);
                            } else if (roll == 1) {
                                worldgenlevel.setBlock(targetPos, config.getPrimaryStone(), UPDATE_ALL);
                            } else if (roll == 2) {
                                if (randomsource.nextInt(8) == 0) {
                                    worldgenlevel.setBlock(targetPos, config.getDeposit(), UPDATE_ALL);
                                } else {
                                    worldgenlevel.setBlock(targetPos, config.getMineralStone(), UPDATE_ALL);
                                }
                            }
                        } else {
                            worldgenlevel.setBlock(targetPos, config.getMineralStone(), UPDATE_ALL);
                        }
                        if (i <= 0) {
                            BlockPos getFromTarget = targetPos;
                            while (worldgenlevel.isEmptyBlock(getFromTarget.below())) {
                                if (randomsource.nextBoolean()) {
                                    worldgenlevel.setBlock(getFromTarget, config.getPrimaryStone(), UPDATE_ALL);
                                } else {
                                    worldgenlevel.setBlock(getFromTarget, config.getSecondaryStone(), UPDATE_ALL);
                                }
                                getFromTarget = getFromTarget.below();
                            }
                        }
                        current = current.add(per);
                    }
                }
            }
        }
        String deposit = config.getDeposit().getBlock().getName().getString();
        Metallurgica.LOGGER.info("{} GENERATED AT: {} {} {}", deposit, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
    
    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }
}
