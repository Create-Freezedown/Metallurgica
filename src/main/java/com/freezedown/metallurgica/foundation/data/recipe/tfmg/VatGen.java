package com.freezedown.metallurgica.foundation.data.recipe.tfmg;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.datagen.recipes.builder.VatMachineRecipeBuilder;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.infastructure.material.MaterialHelper;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.registry.MetallurgicaFluids;
import com.freezedown.metallurgica.registry.material.MetMaterials;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;

import java.util.ArrayList;

public class VatGen extends MProcessingRecipeGen {

    GeneratedRecipe

    sodiumHydroxide = createVatRecipe("sodium_hydroxide", (b) -> (VatMachineRecipeBuilder)b
            .require(I.salt())
            .require(I.salt())
            .require(F.water(), 1000)
            .output(F.sodiumHydroxide(), 600)
            .output(F.chlorine(), 400)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200), this.electrolysis()),

    sodiumHydroxideDecontaminated = createVatRecipe("sodium_hydroxide_from_decontaminated_water", (b) -> (VatMachineRecipeBuilder)b
            .require(I.salt())
            .require(I.salt())
            .require(F.decontaminatedWater(), 1000)
            .output(F.sodiumHydroxide(), 800)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200), this.electrolysis()),

    magnesiumChloride = createVatRecipe("magnesium_chloride", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaFluids.magnesiumChloride.get(), 500)
            .output(MaterialHelper.getFluid(MetMaterials.MAGNESIUM.get(), FlagKey.MOLTEN), 300)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(600), this.electrolysis())

            ;

    public VatGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.VAT_MACHINE_RECIPE;
    }

    public VatMachineRecipeBuilder.VatRecipeParams electrolysis() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:electrode");
        params.machines.add("tfmg:electrode");
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams mixing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:mixing");
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams centrifuge() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:centrifuge");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams freezing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:freezing");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams intenseFreezing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams arcBlasting() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.minSize = 9;
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }
}
