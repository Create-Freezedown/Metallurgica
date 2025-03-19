package com.freezedown.metallurgica.foundation.item.registry;

import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("DataFlowIssue")
@Getter
public class MaterialEntry {

    private final MetallurgicaRegistrate registrate;
    private final Material material;

    public MaterialEntry(MetallurgicaRegistrate registrate, Material material) {
        this.registrate = registrate;
        this.material = material;
    }


    private ResourceLocation matAssetLoc(Material material, String name) {
        return new ResourceLocation(material.getModid(), "item/materials/"+material.getName()+"/" + name);
    }
}
