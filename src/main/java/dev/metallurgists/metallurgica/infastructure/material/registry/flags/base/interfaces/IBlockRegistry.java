package dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces;

import dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.foundation.util.TextUtil;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IBlockRegistry extends IFlagRegistry {

    BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate);

    @Override
    default String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("block.%s.%s", material.getNamespace(), getIdPattern().formatted(material.getName()));
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
