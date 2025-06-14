package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.WireColours;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.block.SheetmetalFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.block.StorageBlockFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.fluid.MoltenFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.item.*;
import com.freezedown.metallurgica.foundation.item.registry.flags.other.CableFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;

public class NonMetalMaterials {
    public static void register() {
        // Null :3
        NULL = new Material.Builder(Metallurgica.asResource("null"))
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
                        new RubbleFlag().crushing().bonusChance(0.01f)
                ).buildAndRegister();

        // --- Actual Non-Metal Materials --- //
        SILICON = new Material.Builder(Metallurgica.asResource("silicon"))
                .element(MetallurgicaElements.SILICON)
                .addFlags(
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new MoltenFlag(1414.0),
                        new DustFlag()
                ).buildAndRegister();

        GRAPHITE = new Material.Builder(Metallurgica.asResource("graphite"))
                .element(MetallurgicaElements.CARBON)
                .addFlags(
                        new MineralFlag(),
                        new MoltenFlag(3652.0),
                        new DustFlag()
        ).buildAndRegister();

        COAL_COKE = new Material.Builder(Metallurgica.asResource("coal_coke"))
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,DUST,STORAGE_BLOCK)
                .existingIds(GEM, "tfmg:coal_coke")
                .addFlags(
                        new StorageBlockFlag("tfmg"),
                        new GemFlag("tfmg"),
                        new DustFlag("tfmg", false)
        ).buildAndRegister();

        COAL = new Material.Builder(Metallurgica.asResource("coal"))
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:coal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new DustFlag()
        ).buildAndRegister();

        DIAMOND = new Material.Builder(Metallurgica.asResource("diamond"))
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM, STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:diamond")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new NuggetFlag(true),
                        new DustFlag()
        ).buildAndRegister();

        CHARCOAL = new Material.Builder(Metallurgica.asResource("charcoal"))
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM)
                .existingIds(GEM, "minecraft:charcoal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
        ).buildAndRegister();

        SULFUR = new Material.Builder(Metallurgica.asResource("sulfur"))
                .element(MetallurgicaElements.SULFUR)
                .noRegister(DUST)
                .addFlags(
                        new DustFlag("tfmg", false)
                ).buildAndRegister();
    }
}
