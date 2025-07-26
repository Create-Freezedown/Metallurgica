package dev.metallurgists.metallurgica.registry.material.init;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IPartialHolder;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class MetMaterialPartialModels {
    public static ImmutableTable.Builder<FlagKey<?>, Material, PartialModel> MATERIAL_PARTIALS_BUILDER = ImmutableTable.builder();
    public static Table<FlagKey<?>, Material, PartialModel> MATERIAL_PARTIALS;

    public static void generateMaterialPartials() {
        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : FlagKey.getAllFlags()) {
                var flag = material.getFlag(flagKey);
                if (flag instanceof IPartialHolder partialHolder) {
                    generatePartialModel(partialHolder.getModelLocation(material), flagKey, material);
                }
            }
        }
    }

    private static void generatePartialModel(ResourceLocation location, FlagKey<?> flagKey, Material material) {
        PartialModel model = block(location);
        MATERIAL_PARTIALS_BUILDER.put(flagKey, material, model);
    }

    private static PartialModel block(ResourceLocation location) {
        return PartialModel.of(new ResourceLocation(location.getNamespace(), String.join("", "block/", location.getPath())));
    }

    public static PartialModel getPartial(Material material, FlagKey<?> flagKey) {
        if (!MATERIAL_PARTIALS.contains(flagKey, material)) throw new IllegalArgumentException("No such partial model is present");
        return MATERIAL_PARTIALS.get(flagKey, material);
    }

    public static PartialModel getPartialUnsafe(Material material, FlagKey<?> flagKey) {
        var builtTable = MATERIAL_PARTIALS_BUILDER.build();
        if (!builtTable.contains(flagKey, material)) throw new IllegalArgumentException("No such partial model is present");
        return builtTable.get(flagKey, material);
    }

    public static void clientInit() {
        generateMaterialPartials();
        MATERIAL_PARTIALS = MATERIAL_PARTIALS_BUILDER.build();
        MATERIAL_PARTIALS_BUILDER = null;
    }
}
