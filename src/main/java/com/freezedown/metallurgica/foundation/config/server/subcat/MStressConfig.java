package com.freezedown.metallurgica.foundation.config.server.subcat;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MStressConfig extends ConfigBase implements BlockStressValues.IStressValueProvider {
    private final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> capacities = new HashMap();
    private final Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> impacts = new HashMap();
    
    public MStressConfig() {
    }
    
    public void registerAll(ForgeConfigSpec.Builder builder) {
        builder.comment(new String[]{".", Comments.su, Comments.impact}).push("impact");
        BlockStressDefaults.DEFAULT_IMPACTS.forEach((r, i) -> {
            if (r.getNamespace().equals("metallurgica")) {
                this.getImpacts().put(r, builder.define(r.getPath(), i));
            }
            
        });
        builder.pop();
        builder.comment(new String[]{".", Comments.su, Comments.capacity}).push("capacity");
        BlockStressDefaults.DEFAULT_CAPACITIES.forEach((r, i) -> {
            if (r.getNamespace().equals("metallurgica")) {
                this.getCapacities().put(r, builder.define(r.getPath(), i));
            }
            
        });
        builder.pop();
    }
    
    public String getName() {
        return "stressValues";
    }
    
    public double getImpact(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = this.getImpacts().get(key);
        return value != null ? (Double)value.get() : 0.0;
    }
    
    public double getCapacity(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        ForgeConfigSpec.ConfigValue<Double> value = this.getCapacities().get(key);
        return value != null ? (Double)value.get() : 0.0;
    }
    
    public boolean hasImpact(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        return this.getImpacts().containsKey(key);
    }
    
    public boolean hasCapacity(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        return this.getCapacities().containsKey(key);
    }
    
    public @Nullable Couple<Integer> getGeneratedRPM(Block block) {
        block = this.redirectValues(block);
        ResourceLocation key = RegisteredObjects.getKeyOrThrow(block);
        Supplier<Couple<Integer>> supplier = BlockStressDefaults.GENERATOR_SPEEDS.get(key);
        return supplier == null ? null : supplier.get();
    }
    
    protected Block redirectValues(Block block) {
        return block;
    }
    
    public Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> getImpacts() {
        return this.impacts;
    }
    
    public Map<ResourceLocation, ForgeConfigSpec.ConfigValue<Double>> getCapacities() {
        return this.capacities;
    }
    
    private static class Comments {
        static String su = "[in Stress Units]";
        static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives";
        static String capacity = "Configure how much stress a source can accommodate for.";
        
        private Comments() {
        }
    }
}
