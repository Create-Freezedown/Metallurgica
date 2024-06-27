package com.freezedown.metallurgica.foundation;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class MBlockStateGen {
    public static <P extends Block> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> naturalMineralTypeBlock(
            String type) {
        return (c, p) -> {
            ConfiguredModel[] variants = new ConfiguredModel[4];
            for (int i = 0; i < variants.length; i++)
                variants[i] = ConfiguredModel.builder()
                        .modelFile(p.models()
                                .cubeAll(type + "_natural_" + i, p.modLoc("block/palettes/stone_types/mineral/" + type + "_" + i)))
                        .buildLast();
            p.getVariantBuilder(c.get())
                    .partialState()
                    .setModels(variants);
        };
    }
}
