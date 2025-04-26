package com.freezedown.metallurgica.compat.kubejs;

import com.freezedown.metallurgica.compat.kubejs.builder.fluid.AcidFluidBuilder;
import com.freezedown.metallurgica.compat.kubejs.builder.fluid.VirtualFluidBuilder;
import com.freezedown.metallurgica.compat.kubejs.builder.item.AcidBarrelItemBuilder;
import com.freezedown.metallurgica.compat.kubejs.schemas.ProcessingRecipeSchema;
import com.freezedown.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class KubeJSMetallurgicaPlugin extends KubeJSPlugin {

    private static final Map<MetallurgicaRecipeTypes, RecipeSchema> recipeSchemas = new HashMap<>();

    static {
        recipeSchemas.put(MetallurgicaRecipeTypes.electrolysis, ProcessingRecipeSchema.PROCESSING_DEFAULT);
        recipeSchemas.put(MetallurgicaRecipeTypes.shaking, ProcessingRecipeSchema.PROCESSING_DEFAULT);
    }

    @Override
    public void init() {
        RegistryInfo.FLUID.addType("metallurgica:virtual", VirtualFluidBuilder.class, VirtualFluidBuilder::new);
        RegistryInfo.FLUID.addType("metallurgica:acid", AcidFluidBuilder.class, AcidFluidBuilder::new);
    }

    @Override
    public void clientInit() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::itemColors);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        for (var createRecipeType : MetallurgicaRecipeTypes.values()) {
            if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
                var schema = recipeSchemas.getOrDefault(createRecipeType, ProcessingRecipeSchema.PROCESSING_DEFAULT);
                event.register(createRecipeType.getId(), schema);
            }
        }
    }

    private void itemColors(RegisterColorHandlersEvent.Item event) {
        for (var builder : RegistryInfo.ITEM) {
            if (builder instanceof AcidBarrelItemBuilder b && b.acidBuilder.bucketColor != 0xFFFFFFFF) {
                event.register((stack, index) -> index == 1 ? b.acidBuilder.bucketColor : 0xFFFFFFFF, b.get());
            }
        }
    }
}
