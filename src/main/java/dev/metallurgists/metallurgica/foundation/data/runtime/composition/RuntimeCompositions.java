package dev.metallurgists.metallurgica.foundation.data.runtime.composition;

import dev.metallurgists.metallurgica.foundation.data.custom.composition.FinishedComposition;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip.MaterialCompositionBuilder;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;

import java.util.List;
import java.util.function.Consumer;

public class RuntimeCompositions {
    public static void compositionAddition(Consumer<FinishedComposition> originalConsumer) {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            createComposition(originalConsumer, material, material.getComposition());
        }
    }

    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, Material material, List<SubComposition> subCompositions) {
        MaterialCompositionBuilder.create(material, subCompositions).save(pFinishedCompositionConsumer);
    }
}
