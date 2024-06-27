package com.freezedown.metallurgica.foundation.worldgen.feature.configuration;

import com.freezedown.metallurgica.foundation.worldgen.MBaseConfigDrivenOreFeatureConfiguration;
import com.freezedown.metallurgica.foundation.worldgen.MOreFeatureConfigEntry;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class MConfigDrivenSpecialOreFeatureConfiguration extends MBaseConfigDrivenOreFeatureConfiguration {
    public static final Codec<MConfigDrivenSpecialOreFeatureConfiguration> CODEC = RecordCodecBuilder.create((p_67849_) -> {
        return p_67849_.group(
                MOreFeatureConfigEntry.CODEC
                        .fieldOf("entry")
                        .forGetter(config -> config.entry),
                Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter((p_161020_) -> {
                    return p_161020_.discardChanceOnAirExposure;
                }),
                Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter((p_161027_) -> {
                    return p_161027_.targetStates;
                }), Codec.floatRange(0.0F, 1.0F).fieldOf("density").orElse(0.0F).forGetter((p_236567_0_) -> {
                    return p_236567_0_.density;
                }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance").orElse(0.0F).forGetter((p_236567_0_) -> {
                    return p_236567_0_.chance;
                })).apply(p_67849_, MConfigDrivenSpecialOreFeatureConfiguration::new);
    });
    
    public final List<TargetBlockState> targetStates;
    public final float density;
    public final float chance;
    
    public MConfigDrivenSpecialOreFeatureConfiguration(MOreFeatureConfigEntry entry, float discardChance, List<TargetBlockState> targets, float density, float spawnChance) {
        super(entry, discardChance);
        this.density = density;
        this.chance = spawnChance;
        this.targetStates = targets;
    }
    
    public MConfigDrivenSpecialOreFeatureConfiguration(MOreFeatureConfigEntry entry, float discardChance, RuleTest test, BlockState state, float density, float spawnChance) {
        this(entry, discardChance, ImmutableList.of(new TargetBlockState(test, state)), density, spawnChance);
    }
    
    public static TargetBlockState target(RuleTest p_161022_, BlockState p_161023_) {
        return new TargetBlockState(p_161022_, p_161023_);
    }
    
    public static class TargetBlockState {
        public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create((p_161039_) -> {
            return p_161039_.group(RuleTest.CODEC.fieldOf("target").forGetter((p_161043_) -> {
                return p_161043_.target;
            }), BlockState.CODEC.fieldOf("state").forGetter((p_161041_) -> {
                return p_161041_.state;
            })).apply(p_161039_, TargetBlockState::new);
        });
        public final RuleTest target;
        public final BlockState state;
        
        TargetBlockState(RuleTest p_161036_, BlockState p_161037_) {
            this.target = p_161036_;
            this.state = p_161037_;
        }
    }
}
