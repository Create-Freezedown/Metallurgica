package com.freezedown.metallurgica.infastructure.material.registry.flags.base;

import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.tterrag.registrate.util.entry.ItemEntry;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class ItemFlag implements IMaterialFlag, IItemRegistry {

    private final String idPattern;
    private String existingNamespace = "metallurgica";
    @Setter
    public List<String> tagPatterns = List.of();

    public ItemFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public ItemFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }


    public abstract ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull MetallurgicaRegistrate registrate);
}
