package com.freezedown.metallurgica.foundation.data;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.data.advancement.MetallurgicaAdvancements;
import com.freezedown.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import com.freezedown.metallurgica.foundation.data.recipe.create.MSequencedAssemblyGen;
import com.freezedown.metallurgica.foundation.data.recipe.vanilla.MStandardRecipeGen;
import com.freezedown.metallurgica.foundation.units.MetallurgicaUnits;
import com.freezedown.metallurgica.registry.MetallurgicaBiomeTemperatures;
import com.freezedown.metallurgica.registry.MetallurgicaCompositions;
import com.freezedown.metallurgica.registry.MetallurgicaElements;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.infrastructure.data.CreateDatagen;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class MetallurgicaDatagen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();
        
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        
        boolean client = event.includeClient();
        boolean server = event.includeServer();
        generator.addProvider(server, new MetallurgicaAdvancements(generator));
        if (server) {
            generator.addProvider(true, new MStandardRecipeGen(generator));
            MProcessingRecipeGen.registerAll(generator);
            MetallurgicaCompositions.register(generator);
            MetallurgicaBiomeTemperatures.register(generator);
            generator.addProvider(true, new  MSequencedAssemblyGen(generator));
        }
    }
    
    private static void addExtraRegistrateData() {
        MetallurgicaRegistrateTags.addGenerators();
        
        Metallurgica.registrate.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("compositions", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            MetallurgicaAdvancements.provideLang(langConsumer);
            MetallurgicaElements.provideElementLang(langConsumer);
            MetallurgicaUnits.provideUnitLang(langConsumer);
        });
    }
    
    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/metallurgica/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}
