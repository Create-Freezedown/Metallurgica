package com.freezedown.metallurgica.foundation.config;

import com.drmangotea.tfmg.TFMG;
import com.freezedown.metallurgica.infastructure.conductor.Conductor;
import com.freezedown.metallurgica.infastructure.conductor.ConductorBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

@SuppressWarnings("UnstableApiUsage")
public class TFMGConductor extends ConfigBase {
    // bump this version to reset configured values.
    private static final int VERSION = 2;

    // IDs need to be used since configs load before registration

    private static final Object2DoubleMap<ResourceLocation> DEFAULT_RESISTIVITIES = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_MAX_LENGTHS = new Object2DoubleOpenHashMap<>();

    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> resistivities = new HashMap<>();
    protected final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> maxLengths = new HashMap<>();

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(".", Comments.resistivity).push("resistivity");
        DEFAULT_RESISTIVITIES.forEach((id, value) -> this.resistivities.put(id, builder.define(id.getPath(), value)));
        builder.pop();

        builder.comment(".", Comments.maxLength).push("max_length");
        DEFAULT_MAX_LENGTHS.forEach((id, value) -> this.maxLengths.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    @Override
    public String getName() {
        return "conductorValues.v" + VERSION;
    }

    @Nullable
    public DoubleSupplier getResistivity(Conductor conductor) {
        ResourceLocation id = conductor.getKey();
        ForgeConfigSpec.ConfigValue<Double> value = this.resistivities.get(id);
        return value == null ? null : value::get;
    }

    @Nullable
    public DoubleSupplier getMaxLength(Conductor conductor) {
        ResourceLocation id = conductor.getKey();
        ForgeConfigSpec.ConfigValue<Double> value = this.maxLengths.get(id);
        return value == null ? null : value::get;
    }

    public static <B extends Conductor, P> NonNullUnaryOperator<ConductorBuilder<B, P>> setNoResistivity() {
        return setResistivity(0);
    }

    public static <B extends Conductor, P> NonNullUnaryOperator<ConductorBuilder<B, P>> setResistivity(double value) {
        return builder -> {
            //assertFromCreate(builder);
            ResourceLocation id = TFMG.asResource(builder.getName());
            DEFAULT_RESISTIVITIES.put(id, value);
            return builder;
        };
    }

    public static <B extends Conductor, P> NonNullUnaryOperator<ConductorBuilder<B, P>> setMaxLength(double value) {
        return builder -> {
            //assertFromCreate(builder);
            ResourceLocation id = TFMG.asResource(builder.getName());
            DEFAULT_MAX_LENGTHS.put(id, value);
            return builder;
        };
    }

    private static class Comments {
        static String resistivity = "Configure the individual resistivity of conductors.";
        static String maxLength = "Configure how much distance a single wire can be used across.";
    }
}
