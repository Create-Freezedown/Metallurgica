package com.freezedown.metallurgica.foundation.data.recipe.metallurgica;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.items.reaction.FluidReactionRecipeBuilder;
import com.freezedown.metallurgica.foundation.data.recipe.MetallurgicaRecipeProvider;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.material.MaterialHelper;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.google.common.base.Supplier;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import java.util.function.UnaryOperator;

public class MFluidReactionGen extends MetallurgicaRecipeProvider {

    private Marker debugMarker = enterFolder("debug");

    //GeneratedRecipe
//
    //    testReaction = create(MaterialHelper.getItem(MetMaterials.CASSITERITE, FlagKey.MINERAL)::asItem, new ProcessingOutput(MetallurgicaItems.alluvialCassiterite.asStack(), 1))
    //        .recipe((b) -> b
    //                .fluid(FluidIngredient.fromFluid(MetallurgicaFluids.decontaminatedWater.get(), 1))
    //                .removeFluid()
    //                .group("cassiterite")
    //        );

    String currentFolder = "";

    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }
    GeneratedRecipeBuilder create(Supplier<? extends ItemLike> input, ProcessingOutput result) {
        return new GeneratedRecipeBuilder(currentFolder, input, result);
    }

    class GeneratedRecipeBuilder {
        private String path;
        private String suffix;
        private Supplier<? extends ItemLike> input;
        private ProcessingOutput result;
        private Supplier<? extends Fluid> fluid;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.suffix = "";
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> input, ProcessingOutput result) {
            this(path);
            this.input = input;
            this.result = result;
        }



        GeneratedRecipe recipe(UnaryOperator<FluidReactionRecipeBuilder> builder) {
            return register(consumer -> {
                FluidReactionRecipeBuilder b = builder.apply(new FluidReactionRecipeBuilder(input.get(), result));
                b.save(consumer, createLocation());
            });
        }

        private ResourceLocation createLocation() {
            return Metallurgica.asResource("fluid_reactions/" + path + "/" + CatnipServices.REGISTRIES.getKeyOrThrow(result.getStack().getItem()).getPath() + suffix);
        }
    }
    public MFluidReactionGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    public String getName() {
        return "Metallurgica Fluid Reactions";
    }
}
