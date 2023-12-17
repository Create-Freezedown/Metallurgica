package com.freezedown.metallurgica.content.world;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.world.striated.MetallurgicaLayeredPatterns;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.ForgeConfigSpec;

public class MetallurgicaOreFeatureConfigEntries {
    public static final OreFeatureConfigEntry STRIATED_GEODES_OVERWORLD =
            create("striated_geodes_overworld", 48, 1 / 32f, -50, 70)
                    .layeredDatagenExt()
                    .withLayerPattern(MetallurgicaLayeredPatterns.amethyst)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();
    
    private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency,
                                                int minHeight, int maxHeight) {
        ResourceLocation id = Metallurgica.asResource(name);
        OreFeatureConfigEntry configDrivenFeatureEntry = new OreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
        return configDrivenFeatureEntry;
    }
    public static void fillConfig(ForgeConfigSpec.Builder builder, String namespace) {
        OreFeatureConfigEntry.ALL
                .forEach((id, entry) -> {
                    if (id.getNamespace().equals(namespace)) {
                        builder.push(entry.getName());
                        entry.addToConfig(builder);
                        builder.pop();
                    }
                });
    }
    public static void init() {}
}
