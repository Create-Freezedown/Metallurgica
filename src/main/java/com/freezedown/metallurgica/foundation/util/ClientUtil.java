package com.freezedown.metallurgica.foundation.util;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

public class ClientUtil {
    
    public static Level getWorld() {
        return Minecraft.getInstance().level;
    }
    
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
    
    public static String fromId(String key) {
        String s = key.replaceAll("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }
    
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
    
    public static LangBuilder lang() {
        return new LangBuilder(Metallurgica.ID);
    }
    public static LangBuilder number(double d) {
        return lang().text(LangNumberFormat.format(d));
    }
    
    public static LangBuilder temp() {
        return lang().translate("gui.goggles.temperature");
    }
    
    public static LangBuilder celcius() {
        return lang().translate("generic.unit.celcius");
    }
    
    public static LangBuilder fahrenheit() {
        return lang().translate("generic.unit.fahrenheit");
    }
    
    public static LangBuilder temperature(double temperature) {
        return MetallurgicaConfigs.client().imAmerican.get() ? number(temperature).space().add(celcius()) : CelciusToFahrenheit(temperature);
    }
    
    public static LangBuilder CelciusToFahrenheit(double celcius) {
        double fahrenheit = celcius * 9 / 5 + 32;
        return number(fahrenheit).space().add(fahrenheit());
    }
    
    private static final int SMALL_DOWN_NUMBER_BASE = '\u2080';
    private static final int SMALL_UP_NUMBER_BASE = '\u2070';
    private static final int SMALL_UP_NUMBER_TWO = '\u00B2';
    private static final int SMALL_UP_NUMBER_THREE = '\u00B3';
    private static final int NUMBER_BASE = '0';
    
    public static String toSmallUpNumbers(String string) {
        return checkNumbers(string, SMALL_UP_NUMBER_BASE, true);
    }
    
    public static String toSmallDownNumbers(String string) {
        return checkNumbers(string, SMALL_DOWN_NUMBER_BASE, false);
    }
    
    @NotNull
    private static String checkNumbers(String string, int smallUpNumberBase, boolean isUp) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int relativeIndex = charArray[i] - NUMBER_BASE;
            if (relativeIndex >= 0 && relativeIndex <= 9) {
                if (isUp) {
                    if (relativeIndex == 2 ) {
                        charArray[i] = SMALL_UP_NUMBER_TWO;
                        continue;
                    } else if (relativeIndex == 3) {
                        charArray[i] = SMALL_UP_NUMBER_THREE;
                        continue;
                    }
                }
                int newChar = smallUpNumberBase + relativeIndex;
                charArray[i] = (char) newChar;
            }
        }
        return new String(charArray);
    }
    
    public static String toRomanNumeral(int number) {
        return "I".repeat(number)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }
}
