package com.freezedown.metallurgica.foundation.item.registry.flags.fluid;

import com.freezedown.metallurgica.content.fluids.types.MaterialFluid;
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
    public FluidEntry<? extends MaterialFluid> registerFluid(@NotNull Material material, FluidFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        return null;
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
