package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.block.IMaterialBlock;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IConditionalComposition;
import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.Getter;
import lombok.Setter;
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

    public abstract BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate);

    public abstract boolean shouldHaveComposition();

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
