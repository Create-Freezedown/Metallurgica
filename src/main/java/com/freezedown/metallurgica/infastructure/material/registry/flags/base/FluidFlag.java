package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.fluid.IMaterialFluid;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.tterrag.registrate.util.entry.FluidEntry;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class FluidFlag implements IMaterialFlag, IIdPattern {
    @Getter
    private final String idPattern;
    private String existingNamespace = "metallurgica";

    public FluidFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public FluidFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public abstract FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, FluidFlag flag, @NotNull MetallurgicaRegistrate registrate);

    public String getUnlocalizedName() {
        return "materialflag." + (this instanceof ISpecialLangSuffix suffix ? suffix.getLangSuffix() : MetallurgicaModels.getFlagName(getKey()));
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getDisplayName());
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("fluid.%s.%s", material.getNamespace(), this.idPattern.formatted(material.getName()));
        if (TextUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    public ResourceLocation getExistingId(Material material) {
        String nameAlternative = material.materialInfo().nameAlternatives().get(getKey());
        if (nameAlternative != null) {
            return new ResourceLocation(existingNamespace, idPattern.formatted(nameAlternative));
        }
        return new ResourceLocation(existingNamespace, idPattern.formatted(material.getName()));
    }
}
