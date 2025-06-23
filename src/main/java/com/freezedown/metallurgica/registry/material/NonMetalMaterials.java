package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.infastructure.material.registry.flags.block.SheetmetalFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.*;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
import static com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey.*;


public class NonMetalMaterials {
    public static void register() {
        // Null :3
        NULL = createMaterial("null", (b) -> b
                .element(MetallurgicaElements.NULL)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetmetalFlag().requiresCompacting(),
                        new SheetFlag().pressTimes(5),
                        new MoltenFlag(9999.0),
                        new DustFlag(),
                        new CableFlag(1, WireColours.missing),
                        new MineralFlag(true),
                        new RubbleFlag().crushing().bonusChance(0.001f)
                ));

        // --- Actual Non-Metal Materials --- //
        SILICON = createMaterial("silicon", (b) -> b
                .element(MetallurgicaElements.SILICON)
                .noRegister(INGOT)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1414.0),
                        new DustFlag()
                ));

        GRAPHITE = createMaterial("graphite", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .addFlags(
                        new MineralFlag(),
                        new MoltenFlag(3652.0),
                        new DustFlag()
        ));

        COAL_COKE = createMaterial("coal_coke", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,DUST,STORAGE_BLOCK)
                .existingIds(GEM, "tfmg:coal_coke")
                .addFlags(
                        new StorageBlockFlag("tfmg"),
                        new GemFlag("tfmg"),
                        new DustFlag("tfmg", false)
        ));

        COAL = createMaterial("coal", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:coal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new DustFlag()
        ));

        DIAMOND = createMaterial("diamond", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM, STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:diamond")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new NuggetFlag(true),
                        new DustFlag()
        ));

        CHARCOAL = createMaterial("charcoal", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM)
                .existingIds(GEM, "minecraft:charcoal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
        ));

        SULFUR = createMaterial("sulfur", (b) -> b
                .element(MetallurgicaElements.SULFUR)
                .noRegister(DUST)
                .addFlags(
                        new DustFlag("tfmg", false)
                ));

        QUARTZ = createMaterial("quartz", (b) -> b
                .composition(MetallurgicaElements.SILICON, 1, MetallurgicaElements.OXYGEN, 2)
                .noRegister(GEM)
                .existingIds(GEM, "minecraft:quartz")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
                ));
    }
}
