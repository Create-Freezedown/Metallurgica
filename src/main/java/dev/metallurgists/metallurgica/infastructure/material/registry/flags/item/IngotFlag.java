package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

import com.drmangotea.tfmg.recipes.CastingRecipe;
import dev.metallurgists.metallurgica.foundation.data.runtime.RuntimeProcessingRecipeBuilder;
import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.IRecipeHandler;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.metallurgists.metallurgica.infastructure.material.MaterialHelper.getNameForRecipe;

public class IngotFlag extends ItemFlag implements IRecipeHandler {
    @Getter
    private boolean requiresCompacting = false;

    public IngotFlag(String existingNamespace) {
        super("%s_ingot", existingNamespace);
        this.setTagPatterns(List.of("c:ingots", "c:ingots/%s"));
    }

    public IngotFlag() {
        this("metallurgica");
    }

    public IngotFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.INGOT;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        var ingot = MaterialHelper.getItem(material, getKey());
        if (material.hasFlag(FlagKey.MOLTEN)) {
            var molten = MaterialHelper.getFluid(material, FlagKey.MOLTEN);
            if (!material.getFlag(FlagKey.MOLTEN).getExistingId(material).getNamespace().equals(material.getNamespace())) return;
            String recipePath = material.asResourceString(getNameForRecipe(material, getKey()) + "_from_" + getNameForRecipe(material, FlagKey.MOLTEN));
            ProcessingRecipeBuilder<CastingRecipe> builder = new RuntimeProcessingRecipeBuilder<>(CastingRecipe::new, provider, recipePath);
            builder.require(molten, MaterialHelper.FluidValues.INGOT);
            builder.output(ingot);
            builder.build();
        }
    }
}
