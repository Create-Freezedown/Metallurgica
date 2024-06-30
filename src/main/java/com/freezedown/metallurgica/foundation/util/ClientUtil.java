package com.freezedown.metallurgica.foundation.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

public class ClientUtil {
    public static List<Double> generateSequenceDoubleStream(double start, double end, double step) {
        return DoubleStream.iterate(start, d -> d <= end, d -> d + step)
                .boxed()
                .collect(toList());
    }
    public static void createCubeOutlineParticle(BlockPos pos, Level level, ParticleOptions particle) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (level.isClientSide()) {
            double minX = mutable.getX();
            double minY = mutable.getY();
            double minZ = mutable.getZ();
            double maxX = mutable.getX() + 1;
            double maxY = mutable.getY() + 1;
            double maxZ = mutable.getZ() + 1;
            List<Double> xList = generateSequenceDoubleStream(minX, maxX, 0.1);
            List<Double> yList = generateSequenceDoubleStream(minY, maxY, 0.1);
            List<Double> zList = generateSequenceDoubleStream(minZ, maxZ, 0.1);
            xList.forEach(x -> {
                level.addParticle(particle, x, mutable.getY(), mutable.getZ() - 0.01, 0, 0, 0);
                level.addParticle(particle, x, mutable.getY(), mutable.getZ() + 1.01, 0, 0, 0);
                level.addParticle(particle, x, mutable.getY() + 1, mutable.getZ() - 0.01, 0, 0, 0);
                level.addParticle(particle, x, mutable.getY() + 1, mutable.getZ() + 1.01, 0, 0, 0);
            });
            zList.forEach(z -> {
                level.addParticle(particle, mutable.getX() - 0.01, mutable.getY(), z, 0, 0, 0);
                level.addParticle(particle, mutable.getX() + 1.01, mutable.getY(), z, 0, 0, 0);
                level.addParticle(particle, mutable.getX() + 0.01, mutable.getY() + 1, z, 0, 0, 0);
                level.addParticle(particle, mutable.getX() + 1.01, mutable.getY() + 1, z, 0, 0, 0);
            });
            yList.forEach(y -> {
                level.addParticle(particle, mutable.getX() - 0.01, y + 0.05, mutable.getZ() - 0.01, 0, 0, 0);
                level.addParticle(particle, mutable.getX() + 0.99, y + 0.05, mutable.getZ() - 0.01, 0, 0, 0);
                level.addParticle(particle, mutable.getX() + 1.01, y + 0.05, mutable.getZ() + 1.01, 0, 0, 0);
                level.addParticle(particle, mutable.getX(), y + 0.05, mutable.getZ() + 1.01, 0, 0, 0);
            });
        }
    }
    public static void createCubeVolumeParticle(BlockPos pos, Level level, ParticleOptions particle) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (level.isClientSide()) {
            double minX = mutable.getX();
            double minY = mutable.getY();
            double minZ = mutable.getZ();
            double maxX = mutable.getX() + 1;
            double maxY = mutable.getY() + 1;
            double maxZ = mutable.getZ() + 1;
            
            // Adjust the step size for denser particle placement
            double step = 0.05;
            
            // Generate particles within the cube volume
            for (double x = minX; x < maxX; x += step) {
                for (double y = minY; y < maxY; y += step) {
                    for (double z = minZ; z < maxZ; z += step) {
                        level.addParticle(particle, x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }
    
    public static void createRandomCubeParticles(BlockPos pos, Level level, List<ParticleOptions> particleTypes) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (level.isClientSide()) {
            double minX = mutable.getX();
            double minY = mutable.getY();
            double minZ = mutable.getZ();
            double maxX = mutable.getX() + 1;
            double maxY = mutable.getY() + 1;
            double maxZ = mutable.getZ() + 1;
            
            Random random = new Random();
            
            // Adjust the step size for denser particle placement
            double step = 0.1;
            
            for (double x = minX; x < maxX; x += step) {
                for (double y = minY; y < maxY; y += step) {
                    for (double z = minZ; z < maxZ; z += step) {
                        ParticleOptions randomParticle = particleTypes.get(random.nextInt(particleTypes.toArray().length));
                        level.addParticle(randomParticle, x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }
    
    public static void createCustomCubeParticles(BlockPos pos, Level level, ParticleOptions smokeParticle, ParticleOptions flameParticle) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (level.isClientSide()) {
            double minX = mutable.getX();
            double minY = mutable.getY();
            double minZ = mutable.getZ();
            double maxX = mutable.getX() + 1;
            double maxY = mutable.getY() + 1;
            double maxZ = mutable.getZ() + 1;
            
            Random random = new Random();
            
            // Adjust the step size for denser particle placement
            double step = 0.1;
            
            for (double x = minX; x < maxX; x += step) {
                for (double y = minY; y < maxY; y += step) {
                    for (double z = minZ; z < maxZ; z += step) {
                        // Randomly choose between smoke and flame particles
                        if (random.nextDouble() < 0.15) {
                            if (random.nextDouble() < 0.15) {
                                if (random.nextDouble() < 0.95) {
                                    level.addParticle(smokeParticle, x, y, z, 0, 0, 0);
                                } else {
                                    level.addParticle(flameParticle, x, y, z, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void createCustomCubeParticles(BlockPos pos, Level level, ParticleOptions particle) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (level.isClientSide()) {
            double minX = mutable.getX();
            double minY = mutable.getY();
            double minZ = mutable.getZ();
            double maxX = mutable.getX() + 1;
            double maxY = mutable.getY() + 1;
            double maxZ = mutable.getZ() + 1;
            
            Random random = new Random();
            
            // Adjust the step size for denser particle placement
            double step = 0.1;
            
            for (double x = minX; x < maxX; x += step) {
                for (double y = minY; y < maxY; y += step) {
                    for (double z = minZ; z < maxZ; z += step) {
                        // Randomly choose between smoke and flame particles
                        if (random.nextDouble() < 0.15) {
                            if (random.nextDouble() < 0.15) {
                                if (random.nextDouble() < 0.95) {
                                    level.addParticle(particle, x, y, z, 0, 0, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
