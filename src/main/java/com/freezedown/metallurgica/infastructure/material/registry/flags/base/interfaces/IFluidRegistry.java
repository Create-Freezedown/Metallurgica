package com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces;

import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IFluidRegistry extends IFlagRegistry {

    FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull MetallurgicaRegistrate registrate);

    @Override
    default String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("fluid.%s.%s", material.getNamespace(), getIdPattern().formatted(material.getName()));
        if (TextUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    @Override
    default String getUnlocalizedName() {
        return "materialflag." + (this instanceof ISpecialLangSuffix suffix ? suffix.getLangSuffix() : MetallurgicaModels.getFlagName(getKey()));
    }

    @Override
    default ResourceLocation getExistingId(Material material) {
        String nameAlternative = material.materialInfo().nameAlternatives().get(getKey());
        if (nameAlternative != null) {
            return new ResourceLocation(getExistingNamespace(), getIdPattern().formatted(nameAlternative));
        }
        return new ResourceLocation(getExistingNamespace(), getIdPattern().formatted(material.getName()));
    }

    FlagKey<?> getKey();
}
