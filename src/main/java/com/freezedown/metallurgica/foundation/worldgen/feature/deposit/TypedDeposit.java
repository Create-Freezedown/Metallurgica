package com.freezedown.metallurgica.foundation.worldgen.feature.deposit;

import com.freezedown.metallurgica.foundation.worldgen.feature.configuration.TypedDepositConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class TypedDeposit extends Feature<TypedDepositConfiguration> {
    public TypedDeposit() {
        super(TypedDepositConfiguration.CODEC);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<TypedDepositConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();
        TypedDepositConfiguration config = context.config();
        
        int tipMin = config.minDepth();
        int tipRand = (int) (config.maxDepth() * 0.3);
        int radiusMin = config.minWidth() / 2;
        int radiusRand = config.maxWidth() / 2;
        
        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int topX = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        int topZ = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        
        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 to = new Vec3(pos.getX() + topX, pos.getY() - tip, pos.getZ() + topZ);
        
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
                                if (randomsource.nextInt(13) == 0) {
                                    if (randomsource.nextBoolean()) worldgenlevel.setBlock(targetPos, config.getPrimaryStone(), UPDATE_ALL); else worldgenlevel.setBlock(targetPos, config.getSecondaryStone(), UPDATE_ALL);
                                } else {
                                    continue;
                                }
                            } else if (roll == 1) {
                                if (randomsource.nextInt(13) == 0) {
                                    if (randomsource.nextBoolean()) worldgenlevel.setBlock(targetPos, config.getSecondaryStone(), UPDATE_ALL); else worldgenlevel.setBlock(targetPos, config.getPrimaryStone(), UPDATE_ALL);
                                }
                            } else if (roll == 2) {
                                if (randomsource.nextInt(13) == 0) {
                                    if (randomsource.nextInt(1000) <= config.depositBlockChance() * 100) worldgenlevel.setBlock(targetPos, config.getDeposit(), UPDATE_ALL); else worldgenlevel.setBlock(targetPos, config.getMineralStone(), UPDATE_ALL);
                                } else {
                                    continue;
                                }
                            }
                        } else {
                            if (randomsource.nextInt(13) == 0) {
                                worldgenlevel.setBlock(targetPos, config.getMineralStone(), UPDATE_ALL);
                            } else {
                                continue;
                            }
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
        return true;
    }
    
    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }
}
