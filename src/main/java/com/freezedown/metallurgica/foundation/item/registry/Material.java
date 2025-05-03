package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.item.registry.flags.*;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.IMaterialFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.infastructure.element.Element;
import com.freezedown.metallurgica.infastructure.element.ElementEntry;
import com.freezedown.metallurgica.infastructure.element.data.ElementData;
import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.data.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(chain = true, fluent = true)
public class Material implements Comparable<Material> {

    @NotNull
    @Getter
    private final MaterialInfo materialInfo;

    @NotNull
    private final MaterialFlags flags;

    @Setter
    @Getter
    private boolean shouldRegister = true;

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

    public boolean noRegister(FlagKey<? extends IMaterialFlag> flag) {
        return flags.getNoRegister().contains(flag);
    }

    public void registerWhen(ConfigBase.ConfigBool config) {
        shouldRegister(config.get());
    }

    public Fluid getFluid(Class<? extends Fluid> fluidClass) {
        FluidFlag flag = getFlag(FlagKey.FLUID);
        if (flag == null) {
            throw new IllegalArgumentException("Material " + getName() + " does not have a Fluid!");
        }

        List<FluidEntry<?>> possibleFluids = MetMaterials.materialFluids.get(this);
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

    public List<ElementData> getComposition() {
        if (materialInfo.composition.isEmpty()) {
            return List.of(new ElementData(MetallurgicaElements.NULL.getId(), 1));
        }
        return materialInfo.composition;
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

    public @NotNull MaterialFlags getFlags() {
        return flags;
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

    public String getUnlocalizedName() {
        return materialInfo.resourceLocation.toLanguageKey("material");
    }

    public MutableComponent getLocalizedName() {
        return Component.translatable(getUnlocalizedName());
    }

    public static class Builder {
        private final MaterialInfo materialInfo;
        private final MaterialFlags flags;
        private final List<ElementData> composition = new ArrayList<>();

        public Builder(ResourceLocation resourceLocation) {
            String name = resourceLocation.getPath();
            if (name.charAt(name.length() - 1) == '_')
                throw new IllegalArgumentException("Material name cannot end with a '_'!");
            materialInfo = new MaterialInfo(resourceLocation);
            flags = new MaterialFlags();
        }

        @SafeVarargs
        public final Builder noRegister(FlagKey<? extends IMaterialFlag>... matFlags) {
            flags.getNoRegister().addAll(Arrays.stream(matFlags).toList());
            return this;
        }

        public Builder withNameAlternative(FlagKey<?> flag, String alternative) {
            materialInfo.nameAlternatives().put(flag, alternative);
            return this;
        }

        public Builder element(ElementEntry<?> entry) {
            materialInfo.composition().add(new ElementData(entry.getId(), 1));
            return this;
        }

        public Builder composition(Object... components) {
            Preconditions.checkArgument(
                    components.length % 2 == 0,
                    "Material Composition list malformed!");

            for (int i = 0; i < components.length; i += 2) {
                if (components[i] == null) {
                    throw new IllegalArgumentException(
                            "ElementData in Compositions List is null for Material " + this.materialInfo.resourceLocation);
                }
                materialInfo.composition().add(new ElementData(components[i] instanceof ElementEntry<?> entry ? entry.getId() : ((Element) components[i]).getId(), ((Number) components[i + 1]).intValue()));
            }
            return this;
        }

        public Builder addFlags(FlagKey<? extends IMaterialFlag>... flags) {
            for (var flag : flags) {
                this.flags.ensureSet(flag);
            }
            return this;
        }

        public Builder fluid(double meltingPoint) {
            FluidFlag fluidFlag = new FluidFlag(meltingPoint);
            flags.setFlag(FlagKey.FLUID, fluidFlag);
            return this;
        }

        public Builder ingot() {
            flags.setFlag(FlagKey.INGOT, new IngotFlag());
            return this;
        }
        public Builder ingot(String existingNamespace) {
            flags.setFlag(FlagKey.INGOT, new IngotFlag(existingNamespace));
            return this;
        }
        public Builder ingot(NonNullFunction<Item.Properties, ? extends Item> factory) {
            flags.setFlag(FlagKey.INGOT, new IngotFlag(factory));
            return this;
        }
        public Builder sheet() {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            flags.setFlag(FlagKey.SHEET, new SheetFlag());
            return this;
        }
        public Builder sheet(String existingNamespace) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            flags.setFlag(FlagKey.SHEET, new SheetFlag(existingNamespace));
            return this;
        }
        public Builder sheet(int pressTimes) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            if (pressTimes < 1) throw new IllegalArgumentException("Sheet cannot be pressed " + pressTimes + "time(s). Must be >1");
            flags.setFlag(FlagKey.SHEET, new SheetFlag().pressTimes(pressTimes));
            if (pressTimes > 1) {
                flags.ensureSet(FlagKey.SEMI_PRESSED_SHEET);
            }
            return this;
        }
        public Builder sheet(int pressTimes, NonNullFunction<Item.Properties, ? extends Item> factory) {
            IngotFlag prop = flags.getFlag(FlagKey.INGOT);
            if (prop == null) ingot();
            if (pressTimes < 1) throw new IllegalArgumentException("Sheet cannot be pressed " + pressTimes + "time(s). Must be >1");
            flags.setFlag(FlagKey.SHEET, new SheetFlag(factory).pressTimes(pressTimes));
            if (pressTimes > 1) {
                flags.ensureSet(FlagKey.SEMI_PRESSED_SHEET);
            }
            return this;
        }

        public Builder wire() {
            SheetFlag prop = flags.getFlag(FlagKey.SHEET);
            if (prop == null) sheet();
            flags.setFlag(FlagKey.WIRE, new WireFlag());
            return this;
        }
        public Builder wire(NonNullFunction<Item.Properties, ? extends Item> factory) {
            SheetFlag prop = flags.getFlag(FlagKey.SHEET);
            if (prop == null) sheet();
            flags.setFlag(FlagKey.WIRE, new WireFlag(factory));
            return this;
        }

        public Builder cable(double resistivity, Pair<int[],int[]> colors) {
            WireFlag prop = flags.getFlag(FlagKey.WIRE);
            if (prop == null) wire();
            if (resistivity < 0) throw new IllegalArgumentException("Resistivity cannot be below 0");
            flags.setFlag(FlagKey.CABLE, new CableFlag(resistivity, colors));
            return this;
        }

        public Builder storageBlock() {
            flags.setFlag(FlagKey.STORAGE_BLOCK, new StorageBlockFlag());
            return this;
        }
        public Builder storageBlock(String existingNamespace) {
            flags.setFlag(FlagKey.STORAGE_BLOCK, new StorageBlockFlag(existingNamespace));
            return this;
        }

        public Material buildAndRegister() {
            var mat = new Material(materialInfo, flags);
            ResourceLocation key = new ResourceLocation(mat.getModid(), mat.getName());
            MetMaterials.registeredMaterials.put(key, mat);
            return mat;
        }
    }

    @Accessors(chain = true)
    public static class MaterialInfo {
        private final ResourceLocation resourceLocation;
        @Getter
        public Map<FlagKey<?>, String> nameAlternatives = new HashMap<>();
        @Getter
        public List<ElementData> composition = new ArrayList<>();

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
