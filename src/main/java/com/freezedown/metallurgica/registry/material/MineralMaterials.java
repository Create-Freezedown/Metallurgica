package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.DustFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.MineralFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.RubbleFlag;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.DUST;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;

public class MineralMaterials {
    public static void register() {
        MALACHITE = new Material.Builder(Metallurgica.asResource("malachite"))
                .composition(MetallurgicaElements.COPPER, 2, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3, MetallurgicaElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();
        MAGNETITE = new Material.Builder(Metallurgica.asResource("magnetite"))
                .composition(MetallurgicaElements.IRON, 3, MetallurgicaElements.OXYGEN, 4)
                .addFlags(new MineralFlag(), new DustFlag()).buildAndRegister();
        HEMATITE = new Material.Builder(Metallurgica.asResource("hematite"))
                .composition(MetallurgicaElements.IRON, 2, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();
        BAUXITE = new Material.Builder(Metallurgica.asResource("bauxite"))
                .composition(MetallurgicaElements.ALUMINUM, 2, MetallurgicaElements.OXYGEN, 3, MetallurgicaElements.HYDROGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();
        SPODUMENE = new Material.Builder(Metallurgica.asResource("spodumene"))
                .composition(MetallurgicaElements.LITHIUM, 1, MetallurgicaElements.ALUMINUM, 1, MetallurgicaElements.SILICON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();
        SPHALERITE = new Material.Builder(Metallurgica.asResource("sphalerite"))
                .composition(MetallurgicaElements.ZINC, 1, MetallurgicaElements.SULFUR, 1)
                .addFlags(new MineralFlag()).buildAndRegister();
        SMITHSONITE = new Material.Builder(Metallurgica.asResource("smithsonite"))
                .composition(MetallurgicaElements.ZINC, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();
        RUTILE = new Material.Builder(Metallurgica.asResource("rutile"))
                .composition(MetallurgicaElements.TITANIUM, 1, MetallurgicaElements.OXYGEN, 2)
                .addFlags(new MineralFlag(), new DustFlag(true), new RubbleFlag().crushing().bonusChance(0.05f)).buildAndRegister();
        POTASH = new Material.Builder(Metallurgica.asResource("potash"))
                .composition(MetallurgicaElements.POTASSIUM, 1, MetallurgicaElements.CARBON, 1, MetallurgicaElements.OXYGEN, 3)
                .addFlags(new MineralFlag()).buildAndRegister();
        CASSITERITE = new Material.Builder(Metallurgica.asResource("cassiterite"))
                .composition(MetallurgicaElements.TIN, 1, MetallurgicaElements.OXYGEN, 2)
                .addFlags(new MineralFlag()).buildAndRegister();
        FLUORITE = new Material.Builder(Metallurgica.asResource("fluorite"))
                .composition(MetallurgicaElements.CALCIUM, 1, MetallurgicaElements.FLUORINE, 2)
                .addFlags(new MineralFlag(), new DustFlag(true)).buildAndRegister();
        CUPRITE = new Material.Builder(Metallurgica.asResource("cuprite"))
                .composition(MetallurgicaElements.COPPER, 1, MetallurgicaElements.OXYGEN, 2)
                .noRegister(DUST)
                .existingIds(DUST, "minecraft:redstone")
                .addFlags(new MineralFlag(), new DustFlag()).buildAndRegister();
    }
}
