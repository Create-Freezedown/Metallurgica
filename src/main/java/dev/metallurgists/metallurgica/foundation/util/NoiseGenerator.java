package dev.metallurgists.metallurgica.foundation.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

public class NoiseGenerator {
    private final SimplexNoise simplexNoise;
    private final double frequency;

    public NoiseGenerator(RandomSource seed, double frequency) {
        this.simplexNoise = new SimplexNoise(seed);
        this.frequency = frequency; // Control the scale of the noise
    }

    public double getNoiseValue(int x, int y, int z) {
        // Scale the coordinates based on the frequency
        double scaledX = x * frequency;
        double scaledY = y * frequency; // You might want to adjust y for height variation
        double scaledZ = z * frequency;

        // Sample the noise
        return simplexNoise.getValue(scaledX, scaledY, scaledZ);
    }
}
