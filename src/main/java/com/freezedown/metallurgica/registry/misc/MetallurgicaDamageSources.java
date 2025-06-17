package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.content.fluids.FluidDamageSource;
import com.simibubi.create.AllDamageTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class MetallurgicaDamageSources {
    public static DamageSource reverbaratory(Level level) {
        return source(MetallurgicaDamageTypes.reverbaratory, level);
    }

    public static DamageSource pressCrushing(Level level) {
        return source(MetallurgicaDamageTypes.crushing, level);
    }

    public static FluidDamageSource acidBurn(Level level, Entity entity, Fluid fluid) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new FluidDamageSource(registry.getHolderOrThrow(MetallurgicaDamageTypes.acidBurn), fluid, entity);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key));
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, @Nullable Entity entity) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key), entity);
    }

    private static DamageSource source(ResourceKey<DamageType> key, LevelReader level, @Nullable Entity causingEntity, @Nullable Entity directEntity) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(key), causingEntity, directEntity);
    }
}
