package com.freezedown.metallurgica.foundation.item.registry.flags.fluid;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.FluidFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ISpecialAssetLocation;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ISpecialLangSuffix;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.NotNull;

public class LiquidFlag extends FluidFlag implements ISpecialAssetLocation, ISpecialLangSuffix {

    public LiquidFlag(String existingNamespace) {
        super("%s", existingNamespace);
    }

    public LiquidFlag() {
        this("metallurgica");
    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, FluidFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate.materialVirtualFluid(this.getIdPattern().formatted(material.getName()), Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"), material, flag)
                .register();
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.LIQUID;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getAssetName() {
        return "liquid";
    }

    @Override
    public String getLangSuffix() {
        return "liquid";
    }
}
