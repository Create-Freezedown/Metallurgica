package com.freezedown.metallurgica.foundation.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.AllOreFeatureConfigEntries;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({AllOreFeatureConfigEntries.class})
public class AllOreFeatureConfigEntriesMixin {
    
    @Shadow
    public static final OreFeatureConfigEntry ZINC_ORE;
    
    @Unique
    private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency, int minHeight, int maxHeight) {
        ResourceLocation id = Create.asResource(name);
        OreFeatureConfigEntry configDrivenFeatureEntry = new OreFeatureConfigEntry(id, clusterSize, frequency, minHeight, maxHeight);
        return configDrivenFeatureEntry;
    }
    
    static {
        ZINC_ORE = create("zinc_ore", 0, 0, 0, 0).standardDatagenExt().withBlocks(Couple.create(AllBlocks.ZINC_ORE, AllBlocks.DEEPSLATE_ZINC_ORE)).biomeTag(BiomeTags.IS_OVERWORLD).parent();
    }
}
