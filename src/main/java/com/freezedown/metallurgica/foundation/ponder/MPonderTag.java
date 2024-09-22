package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.simibubi.create.foundation.ponder.PonderTag;
import net.minecraft.resources.ResourceLocation;

public class MPonderTag extends PonderTag {
    public static final PonderTag MINERALS = create("minerals").defaultLang("Minerals", "Ores and Minerals and how to use them").item(MetallurgicaOre.CASSITERITE.MATERIAL.raw().get(), true, false).addToIndex();
    public static final PonderTag METALS = create("metals").defaultLang("Metals", "Metallurgy and it's many complicated features").item(MetallurgicaItems.bronzeIngot.get(), true, false).addToIndex();
    public static final PonderTag MACHINERY = create("machinery").defaultLang("Machinery", "Advanced Machines and Tools").item(MetallurgicaBlocks.electrolyzer.get(), true, false).addToIndex();
    public static final PonderTag PRIMITIVE = create("primitive").defaultLang("Primitive Utilities", "Early Game Utilities and Tools").item(MetallurgicaItems.dirtyClayBall.get(), true, false).addToIndex();
    
    public MPonderTag(ResourceLocation id) {
        super(id);
    }
    
    private static PonderTag create(String id) {
        return new PonderTag(Metallurgica.asResource(id));
    }
}
