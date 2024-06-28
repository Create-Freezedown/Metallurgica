package com.freezedown.metallurgica.foundation.data;

import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class MetallurgicaRegistrateTags {
    public static void addGenerators() {
        Metallurgica.registrate.addDataGenerator(ProviderType.BLOCK_TAGS, MetallurgicaRegistrateTags::genBlockTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.ITEM_TAGS, MetallurgicaRegistrateTags::genItemTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.FLUID_TAGS, MetallurgicaRegistrateTags::genFluidTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.ENTITY_TAGS, MetallurgicaRegistrateTags::genEntityTags);
    }
    private static void genBlockTags(RegistrateTagsProvider<Block> prov) {
        prov.tag(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag)
                .add(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock().get())
                ;
        
        for (MetallurgicaTags.AllBlockTags tag : MetallurgicaTags.AllBlockTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }
    
    private static void genItemTags(RegistrateTagsProvider<Item> prov) {
        
        for (MetallurgicaTags.AllItemTags tag : MetallurgicaTags.AllItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }
    
    private static void genFluidTags(RegistrateTagsProvider<Fluid> prov) {
        prov.tag(MetallurgicaTags.modFluidTag("fluid_reactive/chlorine")).add(TFMGFluids.GASOLINE.get().getFlowing(), TFMGFluids.GASOLINE.get().getSource());
    }
    
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> prov) {
    
    }
    
}
