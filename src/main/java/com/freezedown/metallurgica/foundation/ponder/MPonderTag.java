package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.foundation.ponder.PonderTag;
import net.minecraft.resources.ResourceLocation;

public class MPonderTag extends PonderTag {
    public static final PonderTag MINERALS = create("minerals");
    public static final PonderTag METALS = create("metals");
    public static final PonderTag MACHINERY = create("machinery");
    public static final PonderTag PRIMITIVE = create("primitive");
    
    public MPonderTag(ResourceLocation id) {
        super(id);
    }
    
    private static PonderTag create(String id) {
        return new PonderTag(Metallurgica.asResource(id));
    }
}
