package com.freezedown.metallurgica.foundation.data;

import com.drmangotea.createindustry.blocks.machines.flarestack.FlarestackBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaMaterials;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_GLASS.tag)
                .add(
                        AllPaletteBlocks.FRAMED_GLASS.get(),
                        AllPaletteBlocks.TILED_GLASS.get(),
                        AllPaletteBlocks.HORIZONTAL_FRAMED_GLASS.get(),
                        AllPaletteBlocks.VERTICAL_FRAMED_GLASS.get()
                )
                .addTag(MetallurgicaTags.AllBlockTags.REVERBARATORY_WALL.tag);
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_WALL.tag)
                .add(
                        MetallurgicaBlocks.carbonBrick.get(),
                        TFMGBlocks.FIREPROOF_BRICKS.get()
                );
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_INPUT.tag)
                .add(
                        AllBlocks.CHUTE.get()
                );
        prov.tag(MetallurgicaTags.AllBlockTags.DEPOSITS.tag)
                .add(
                        MetallurgicaMaterials.FLUORITE.MATERIAL.depositBlock().get()
                );
        prov.tag(MetallurgicaTags.AllBlockTags.AIR_BLOCKING.tag)
                .add(
                        MetallurgicaBlocks.logPile.get(),
                        MetallurgicaBlocks.ashedCharcoalPile.get(),
                        MetallurgicaBlocks.charredLogPile.get(),
                        MetallurgicaBlocks.charcoalPile.get()
                ).addTag(BlockTags.DIRT);
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
        
        prov.tag(MetallurgicaTags.AllItemTags.IGNITES_LOG_PILE.tag).add(
                Items.FLINT_AND_STEEL,
                Items.FIRE_CHARGE
        );
        
        prov.tag(MetallurgicaTags.AllItemTags.NEEDS_CHEMICAL_FORMULA_TOOLTIP.tag).add(
                //Ingots
                Items.IRON_INGOT,
                Items.GOLD_INGOT,
                Items.COPPER_INGOT,
                TFMGItems.ALUMINUM_INGOT.get(),
                AllItems.ZINC_INGOT.get(),
                AllItems.BRASS_INGOT.get(),
                
                //Nuggets
                Items.IRON_NUGGET,
                Items.GOLD_NUGGET,
                AllItems.COPPER_NUGGET.get(),
                AllItems.ZINC_NUGGET.get(),
                AllItems.BRASS_NUGGET.get(),
                
                //Misc Items
                TFMGItems.SULFUR_DUST.get(),
                TFMGBlocks.SULFUR.get().asItem(),
                TFMGItems.NITRATE_DUST.get(),
                TFMGItems.LIMESAND.get()
        );
    }
    
    private static void genFluidTags(RegistrateTagsProvider<Fluid> prov) {
        prov.tag(MetallurgicaTags.modFluidTag("fluid_reactive/chlorine")).add(TFMGFluids.GASOLINE.get().getFlowing(), TFMGFluids.GASOLINE.get().getSource());
        
        prov.tag(MetallurgicaTags.AllFluidTags.REVERBARATORY_FUELS.tag)
                .add(
                        TFMGFluids.BUTANE.getSource(),
                        TFMGFluids.PROPANE.getSource(),
                        TFMGFluids.LPG.getSource(),
                        TFMGFluids.KEROSENE.getSource(),
                        TFMGFluids.NAPHTHA.getSource(),
                        TFMGFluids.ETHYLENE.getSource(),
                        TFMGFluids.PROPYLENE.getSource(),
                        TFMGFluids.DIESEL.getSource(),
                        TFMGFluids.LUBRICATION_OIL.getSource(),
                        TFMGFluids.HEAVY_OIL.getSource(),
                        TFMGFluids.CREOSOTE.getSource(),
                        TFMGFluids.GASOLINE.getSource()
                );
    }
    
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> prov) {
    
    }
    
}
