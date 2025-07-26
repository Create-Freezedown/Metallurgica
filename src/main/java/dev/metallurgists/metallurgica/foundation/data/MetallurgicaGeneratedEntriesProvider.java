package dev.metallurgists.metallurgica.foundation.data;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.worldgen.MBiomeModifiers;
import dev.metallurgists.metallurgica.foundation.worldgen.MetallurgicaConfiguredFeatures;
import dev.metallurgists.metallurgica.foundation.worldgen.MetallurgicaPlacedFeatures;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MetallurgicaGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, MetallurgicaDamageTypes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, MetallurgicaConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, MetallurgicaPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, MBiomeModifiers::bootstrap)
            ;

    public MetallurgicaGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Metallurgica.ID));
    }

    @Override
    public String getName() {
        return "Metallurgica's Generated Registry Entries";
    }
}
