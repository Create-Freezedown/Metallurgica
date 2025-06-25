package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.infastructure.material.registry.flags.item.DustFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.MineralFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.item.RubbleFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.registry.material.MetMaterials.*;
import static com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey.*;


public class MineralMaterials {
    public static void register() {
        MALACHITE = createMaterial("malachite", (b) -> b
                .composition(MetallurgicaElements.COPPER, 2, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3, MetallurgicaElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()));
        MAGNETITE = createMaterial("magnetite", (b) -> b
                .composition(MetallurgicaElements.IRON, 3, MetallurgicaElements.OXYGEN, 4)
                .addFlags(new MineralFlag(), new DustFlag()));
        HEMATITE = createMaterial("hematite", (b) -> b
                .composition(MetallurgicaElements.IRON, 2, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()));
        BAUXITE = createMaterial("bauxite", (b) -> b
                .composition(MetallurgicaElements.ALUMINUM, 2, MetallurgicaElements.OXYGEN, 3, MetallurgicaElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()));
        SPODUMENE = createMaterial("spodumene", (b) -> b
                .composition(MetallurgicaElements.LITHIUM, 1, MetallurgicaElements.ALUMINUM, 1, MetallurgicaElements.SILICON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()));
        SPHALERITE = createMaterial("sphalerite", (b) -> b
                .composition(MetallurgicaElements.ZINC, 1, MetallurgicaElements.SULFUR, 1)
                .addFlags(new MineralFlag()));
        SMITHSONITE = createMaterial("smithsonite", (b) -> b
                .composition(MetallurgicaElements.ZINC, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()));
        RUTILE = createMaterial("rutile", (b) -> b
                .composition(MetallurgicaElements.TITANIUM, 1, MetallurgicaElements.OXYGEN, 2)
                .addFlags(new MineralFlag(), new DustFlag(true), new RubbleFlag().crushing().bonusChance(0.05f)));
        POTASH = createMaterial("potash", (b) -> b
                .composition(MetallurgicaElements.POTASSIUM, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()));
        CASSITERITE = createMaterial("cassiterite", (b) -> b
                .composition(MetallurgicaElements.TIN, 1, MetallurgicaElements.OXYGEN, 2)
                .addFlags(new MineralFlag()));
        FLUORITE = createMaterial("fluorite", (b) -> b
                .composition(MetallurgicaElements.CALCIUM, 1, MetallurgicaElements.FLUORINE, 2)
                .addFlags(new MineralFlag(), new DustFlag(true)));
        CUPRITE = createMaterial("cuprite", (b) -> b
                .composition(MetallurgicaElements.COPPER, 1, MetallurgicaElements.OXYGEN, 2)
                .noRegister(DUST)
                .existingIds(DUST, "minecraft:redstone")
                .addFlags(new MineralFlag(), new DustFlag()));
        VANADINITE = createMaterial("vanadinite", (b) -> b
                .composition(MetallurgicaElements.LEAD, 5, MetallurgicaElements.VANADIUM, 1, MetallurgicaElements.OXYGEN, 4, MetallurgicaElements.CHLORINE, 1)
                .addFlags(new MineralFlag(), new DustFlag(), new RubbleFlag().crushing().bonusChance(0.15f)));
    }
}
