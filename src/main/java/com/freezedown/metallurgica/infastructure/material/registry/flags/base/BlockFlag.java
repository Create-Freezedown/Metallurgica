package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.block.IMaterialBlock;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IConditionalComposition;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IIdPattern;
import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class BlockFlag implements IMaterialFlag, IBlockRegistry, IConditionalComposition {

    private final String idPattern;
    private String existingNamespace = "metallurgica";
    @Setter
    private List<String> tagPatterns = List.of();
    @Setter
    private List<String> itemTagPatterns = List.of();

    public BlockFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public BlockFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    @Override
    public String getUnlocalizedName() {
        return "materialflag." + (this instanceof ISpecialLangSuffix suffix ? suffix.getLangSuffix() : MetallurgicaModels.getFlagName(getKey()));
    }

    public ResourceLocation getExistingId(Material material) {
        String nameAlternative = material.materialInfo().nameAlternatives().get(getKey());
        if (nameAlternative != null) {
            return new ResourceLocation(getExistingNamespace(), getIdPattern().formatted(nameAlternative));
        }
        return new ResourceLocation(getExistingNamespace(), getIdPattern().formatted(material.getName()));
    }

    public abstract BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate);

    public abstract boolean shouldHaveComposition();

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
