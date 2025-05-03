package com.freezedown.metallurgica.registry.material;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.registry.misc.MetallurgicaElements;

import static com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey.*;
import static com.freezedown.metallurgica.registry.material.MetMaterials.*;

public class AlloyMaterials {
    public static void register() {
        TITANIUM_ALUMINIDE = new Material.Builder(Metallurgica.asResource("titanium_aluminide"))
                .composition(MetallurgicaElements.TITANIUM, 2, MetallurgicaElements.ALUMINUM, 1)
                .ingot()
                .storageBlock()
                .sheet(3)
                .fluid(1447.0)
                .buildAndRegister();
        NETHERITE = new Material.Builder(Metallurgica.asResource("netherite"))
                .composition(MetallurgicaElements.NETHERIUM, 1, MetallurgicaElements.GOLD, 1)
                .noRegister(INGOT)
                .ingot("minecraft")
                .sheet()
                .fluid(3562.0)
                .buildAndRegister();
        BRASS = new Material.Builder(Metallurgica.asResource("brass"))
                .composition(MetallurgicaElements.COPPER, 3, MetallurgicaElements.ZINC, 1)
                .noRegister(INGOT, SHEET)
                .ingot("create")
                .sheet("create")
                .fluid(920.0)
                .buildAndRegister();
        BRONZE = new Material.Builder(Metallurgica.asResource("bronze"))
                .composition(MetallurgicaElements.COPPER, 7, MetallurgicaElements.TIN, 2)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(950.0)
                .buildAndRegister();
        ARSENICAL_BRONZE = new Material.Builder(Metallurgica.asResource("arsenical_bronze"))
                .composition(MetallurgicaElements.COPPER, 4, MetallurgicaElements.TIN, 1, MetallurgicaElements.ARSENIC, 3)
                .ingot()
                .storageBlock()
                .sheet()
                .fluid(685.0)
                .buildAndRegister();
    }
}
