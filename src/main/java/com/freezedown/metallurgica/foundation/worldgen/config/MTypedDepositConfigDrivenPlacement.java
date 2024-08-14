package com.freezedown.metallurgica.foundation.worldgen.config;

import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.worldgen.MetallurgicaPlacementModifiers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MTypedDepositConfigDrivenPlacement extends PlacementModifier {
    public static final Codec<MTypedDepositConfigDrivenPlacement> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                MTypedDepositFeatureConfigEntry.CODEC
                        .fieldOf("entry")
                        .forGetter(MTypedDepositConfigDrivenPlacement::getEntry)
        ).apply(instance, MTypedDepositConfigDrivenPlacement::new);
    });
    
    private final MTypedDepositFeatureConfigEntry entry;
    
    public MTypedDepositConfigDrivenPlacement(MTypedDepositFeatureConfigEntry entry) {
        this.entry = entry;
    }
    
    public MTypedDepositFeatureConfigEntry getEntry() {
        return entry;
    }
    
    public float getFrequency() {
        if (MetallurgicaConfigs.common().worldGen.disable.get())
            return 0;
        return entry.frequency.getF();
    }
    
    public int getChance() {
        return entry.chance.get();
    }
    
    protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
        return pRandom.nextFloat() < 1.0F / (float)getChance();
    }
    
    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int count = getCount(getFrequency(), random);
        if (count == 0 || !shouldPlace(context, random, pos)) {
            return Stream.empty();
        }
        
        return IntStream.range(0, count)
                .mapToObj(i -> pos)
                .map(p -> {
                    int x = random.nextInt(16) + p.getX();
                    int z = random.nextInt(16) + p.getZ();
                    int y = random.nextInt(16) - p.getY();
                    return new BlockPos(x, y, z);
                });
    }
    
    public int getCount(float frequency, RandomSource random) {
        int floored = Mth.floor(frequency);
        return floored + (random.nextFloat() < (frequency - floored) * 0.5 ? 1 : 0); // Adjust the threshold
    }
    
    @Override
    public PlacementModifierType<?> type() {
        return MetallurgicaPlacementModifiers.TYPED_DEPOSIT_CONFIG_DRIVEN.get();
    }
    
    public int getMinY() {
        return entry.minHeight.get();
    }
    
    public int getMaxY() {
        return entry.maxHeight.get();
    }
}
