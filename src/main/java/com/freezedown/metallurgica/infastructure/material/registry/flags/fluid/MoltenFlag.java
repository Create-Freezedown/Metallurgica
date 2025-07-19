package com.freezedown.metallurgica.infastructure.material.registry.flags.fluid;

import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.material.item.MaterialBucketItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class MoltenFlag extends FluidFlag {
    public final double meltingPoint;

    public MoltenFlag(double meltingPoint) {
        super("molten_%s", "metallurgica");
        this.meltingPoint = meltingPoint;
    }

    public MoltenFlag(String existingNamespace) {
        super("molten_%s", existingNamespace);
        this.meltingPoint = 100; // Default melting point, can be overridden
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.MOLTEN;
    }

    @ApiStatus.Internal
    public void registerFluids(@NotNull Material material, @NotNull MetallurgicaRegistrate registrate) {
        //List<FluidEntry<?>> fluidEntries = new ArrayList<>();
        //if (material.hasFlag(FlagKey.INGOT)) {
        //    FluidEntry<?> fluid = registrate.moltenMetal(material, meltingPoint);
        //    fluidEntries.add(fluid);
        //}
        //if (!fluidEntries.isEmpty())
        //    MetMaterials.materialFluids.put(material, fluidEntries);
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate.moltenMetal(this.getIdPattern().formatted(material.getName()), material, flag, meltingPoint)
                .bucket((sup, p) -> new MaterialBucketItem(sup, p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
    }
}
