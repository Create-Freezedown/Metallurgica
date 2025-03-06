package com.freezedown.metallurgica.foundation.data;

import com.freezedown.metallurgica.registry.misc.MetallurgicaDamageTypes;
import com.simibubi.create.AllDamageTypes;
import com.simibubi.create.Create;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MetallurgicaGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, MetallurgicaDamageTypes::bootstrap);

    public MetallurgicaGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Create.ID));
    }

    @Override
    public String getName() {
        return "Metallurgica's Generated Registry Entries";
    }
}
