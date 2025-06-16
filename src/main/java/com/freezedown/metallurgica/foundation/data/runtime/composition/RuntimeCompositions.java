package com.freezedown.metallurgica.foundation.data.runtime.composition;

import com.freezedown.metallurgica.foundation.data.custom.composition.FinishedComposition;
import com.freezedown.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionBuilder;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.element.data.ElementData;
import com.freezedown.metallurgica.infastructure.element.data.SubComposition;
import com.freezedown.metallurgica.registry.material.MetMaterials;

import java.util.List;
import java.util.function.Consumer;

public class RuntimeCompositions {
    public static void compositionAddition(Consumer<FinishedComposition> originalConsumer) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            createComposition(originalConsumer, material, ElementData.createFromList(material.getComposition()));
        }
    }

    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, Material material, List<SubComposition> subCompositions) {
        MaterialCompositionBuilder.create(material, subCompositions).save(pFinishedCompositionConsumer);
    }
}
