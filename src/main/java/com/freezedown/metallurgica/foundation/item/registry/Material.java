package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.item.registry.flags.*;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.registry.misc.MetallurgicaMaterials;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.createmod.catnip.data.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Material implements Comparable<Material> {

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    private Material(@NotNull MaterialInfo materialInfo, @NotNull MaterialFlags flags) {
        this.materialInfo = materialInfo;
        this.flags = flags;
        this.flags.setMaterial(this);
        verifyMaterial();
    }

    public String getName() {
        return materialInfo.resourceLocation.getPath();
    }

    public String getModid() {
        return materialInfo.resourceLocation.getNamespace();
    }

    public Fluid getFluid(Class<? extends Fluid> fluidClass) {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getName() + " does not have a Fluid!");
        }

        List<FluidEntry<?>> possibleFluids = MetallurgicaMaterials.materialFluids.get(this);
        FluidEntry<?> toReturn = null;
        for (FluidEntry<?> fluidEntry : possibleFluids) {
            if (!fluidClass.isInstance(fluidEntry.get())) continue;
            toReturn = fluidEntry;
        }
        if (toReturn == null) {
            throw new IllegalArgumentException("Material " + getName() + " does not have a Fluid of type specified: " + fluidClass.getName());
        }
        return toReturn.get();
    }

    public <T extends IMaterialFlag> boolean hasFlag(FlagKey<T> key) {
        return getFlag(key) != null;
    }

    public <T extends IMaterialFlag> T getFlag(FlagKey<T> key) {
        return flags.getFlag(key);
    }

    public <T extends IMaterialFlag> void setFlag(FlagKey<T> key, IMaterialFlag flag) {
        flags.setFlag(key, flag);
        //flags.verify();
    }

    public void verifyMaterial() {
        //flags.verify();
        //this.chemicalFormula = calculateChemicalFormula();
        //calculateDecompositionType();
    }

    @Override
    public int compareTo(@NotNull Material material) {
        return toString().compareTo(material.toString());
    }

    public static class Builder {
        private final MaterialInfo materialInfo;
        private final MaterialFlags flags;

        public Builder(ResourceLocation resourceLocation) {
            String name = resourceLocation.getPath();
            if (name.charAt(name.length() - 1) == '_')
                throw new IllegalArgumentException("Material name cannot end with a '_'!");
            materialInfo = new MaterialInfo(resourceLocation);
            flags = new MaterialFlags();
        }

        public Builder withNameAlternative(FlagKey<?> flag, String alternative) {
            materialInfo.getNameAlternatives().put(flag, alternative);
            return this;
        }

        public Builder fluid(double meltingPoint) {
            FluidFlag fluidFlag = new FluidFlag(meltingPoint);
            flags.setFlag(FlagKey.FLUID, fluidFlag);
            return this;
        }

        public Builder ingot() {
            flags.setFlag(FlagKey.INGOT, new IngotFlag(false));
            return this;
        }
        public Builder ingot(NonNullFunction<Item.Properties, ? extends Item> factory) {
            flags.setFlag(FlagKey.INGOT, new IngotFlag(false).factory(factory));
            return this;
        }
        public Builder ingot(boolean noRegister) {
            flags.setFlag(FlagKey.INGOT, new IngotFlag(noRegister));
            return this;
        }
        public Builder sheet() {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            flags.setFlag(FlagKey.SHEET, new SheetFlag(1));
            return this;
        }

        public Builder sheet(boolean noRegister) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            flags.setFlag(FlagKey.SHEET, new SheetFlag(1, noRegister));
            return this;
        }

        public Builder sheet(int pressTimes) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            if (pressTimes < 1) throw new IllegalArgumentException("Sheet cannot be pressed " + pressTimes + "time(s). Must be >1");
            flags.setFlag(FlagKey.SHEET, new SheetFlag(pressTimes));
            return this;
        }
        public Builder sheet(int pressTimes, NonNullFunction<Item.Properties, ? extends Item> factory) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            if (pressTimes < 1) throw new IllegalArgumentException("Sheet cannot be pressed " + pressTimes + "time(s). Must be >1");
            flags.setFlag(FlagKey.SHEET, new SheetFlag(pressTimes).factory(factory));
            return this;
        }

        public Builder conductor(double resistivity, Pair<int[],int[]> colors) {
            SheetFlag prop = flags.getFlag(FlagKey.SHEET);
            if (prop == null) sheet();
            if (resistivity < 0) throw new IllegalArgumentException("Resistivity cannot be below 0");
            flags.setFlag(FlagKey.WIRING, new WiringFlag(resistivity, colors));
            return this;
        }

        public Material build() {
            var mat = new Material(materialInfo, flags);
            return mat;
        }
    }

    @Accessors(chain = true)
    public static class MaterialInfo {
        private final ResourceLocation resourceLocation;
        @Getter
        public Map<FlagKey<?>, String> nameAlternatives = new HashMap<>();

        private MaterialInfo(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }

        public MaterialInfo withNameAlternative(FlagKey<?> flag, String alternative) {
            nameAlternatives.put(flag, alternative);
            return this;
        }

        private void verifyInfo(MaterialFlags flags) {
            // no-op
        }
    }
}
