package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

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
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
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

public class RubbleFlag extends ItemFlag implements IRecipeHandler {
    @Getter
    private boolean crushing = false;
    @Getter
    private float bonusChance = 0;

    public RubbleFlag(String existingNamespace) {
        super("%s_rubble", existingNamespace);
        this.setTagPatterns(List.of("c:rubble", "c:rubble/%s"));
    }

    public RubbleFlag() {
        this("metallurgica");
    }

    public RubbleFlag crushing() {
        this.crushing = true;
        return this;
    }

    public RubbleFlag bonusChance(float chance) {
        this.bonusChance = chance;
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
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        var mineral = MaterialHelper.getItem(material, FlagKey.MINERAL);
        var rubble = MaterialHelper.getItem(material, getKey());
        if (isCrushing()) {
            String recipePath = material.asResourceString(getNameForRecipe(material, getKey()) + "_from_" + getNameForRecipe(material, FlagKey.MINERAL));
            ProcessingRecipeBuilder<CrushingRecipe> builder = new RuntimeProcessingRecipeBuilder<>(CrushingRecipe::new, provider, recipePath);
            builder.require(mineral);
            builder.output(rubble);
            if (getBonusChance() > 0) {
                builder.output(getBonusChance(), rubble);
            }
            builder.averageProcessingDuration();
            builder.build();
        }
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.RUBBLE;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.MINERAL, true);
    }
}
