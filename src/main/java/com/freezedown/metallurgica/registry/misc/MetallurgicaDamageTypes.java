package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.foundation.damageTypes.DamageTypeBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class MetallurgicaDamageTypes {
    public static final ResourceKey<DamageType>
        reverbaratory = key("reverbaratory"),
        acidBurn = key("acid_burn");

    private static ResourceKey<DamageType> key(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Metallurgica.asResource(name));
    }

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        new DamageTypeBuilder(reverbaratory).effects(DamageEffects.BURNING).register(ctx);
        new DamageTypeBuilder(acidBurn).effects(DamageEffects.BURNING).register(ctx);
    }
}
