package dev.metallurgists.metallurgica.foundation.data.custom.composition.tooltip;

import dev.metallurgists.metallurgica.foundation.data.custom.composition.FinishedComposition;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Consumer;

public class MaterialCompositionBuilder {
    private final Material material;
    private final List<SubComposition> subCompositions;

    public MaterialCompositionBuilder(Material material, List<SubComposition> subCompositions) {
        this.material = material;
        this.subCompositions = subCompositions;
    }

    public static MaterialCompositionBuilder create(Material material, List<SubComposition> subCompositions) {
        return new MaterialCompositionBuilder(material, subCompositions);
    }

    static ResourceLocation getDefaultCompositionId(Material material) {
        return material.getId();
    }

    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        this.save(pFinishedCompositionConsumer, getDefaultCompositionId(this.material));
    }

    public void save(Consumer<FinishedComposition> pFinishedCompositionConsumer, ResourceLocation pCompositionId) {
        Material toApply = this.material;
        pFinishedCompositionConsumer.accept(new DataGenResult(pCompositionId, toApply, this.subCompositions));
    }

    public static class DataGenResult implements FinishedComposition {
        private final Material material;
        private final List<SubComposition> subCompositions;
        private ResourceLocation id;

        public DataGenResult(ResourceLocation pId, Material material, List<SubComposition> subCompositions) {
            this.material = material;
            this.subCompositions = subCompositions;
            this.id = pId;
        }

        @Override
        public void serializeData(JsonObject json) {
            json.addProperty("material", material.getId().toString());
            JsonArray compositionsArray = new JsonArray();
            for (SubComposition subComposition : subCompositions) {
                compositionsArray.add(subComposition.toJson());
            }
            json.add("compositions", compositionsArray);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }
    }
}
