package com.freezedown.metallurgica.foundation.ponder;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaItems;
import com.freezedown.metallurgica.registry.MetallurgicaOre;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class MetallurgicaPonderTags {

    public static final ResourceLocation
            MINERALS = loc("minerals"),
            METALS = loc("metals"),
            PRIMITIVE = loc("primitive"),
            MACHINERY = loc("machinery")
    ;

    private static ResourceLocation loc(String id) {
        return Metallurgica.asResource(id);
    }

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                CatnipServices.REGISTRIES::getKeyOrThrow);

        helper.registerTag(MINERALS)
                .addToIndex()
                .item(MetallurgicaOre.CASSITERITE.ORE.raw().get(), true, false)
                .title("Minerals")
                .description("Ores, Minerals and how to use them")
                .register();

        helper.registerTag(METALS)
                .addToIndex()
                .item(MetallurgicaItems.bronzeIngot.get(), true, false)
                .title("Metals")
                .description("Metallurgy and it's many complicated features")
                .register();

        helper.registerTag(PRIMITIVE)
                .addToIndex()
                .item(MetallurgicaItems.dirtyClayBall.get(), true, false)
                .title("Primitive Utilities")
                .description("Early-Game Utilities and Tools")
                .register();

        helper.registerTag(MACHINERY)
                .addToIndex()
                .item(MetallurgicaBlocks.electrolyzer.get(), true, false)
                .title("Machinery")
                .description("Advanced Machines and Tools")
                .register();

    }
}
