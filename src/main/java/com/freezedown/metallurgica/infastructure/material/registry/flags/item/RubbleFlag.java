package com.freezedown.metallurgica.infastructure.material.registry.flags.item;

import com.freezedown.metallurgica.foundation.data.runtime.RuntimeProcessingRecipeBuilder;
import com.freezedown.metallurgica.foundation.data.runtime.recipe.handler.CrushingRecipeHandler;
import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.foundation.material.item.MaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.MaterialHelper;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.IRecipeHandler;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.freezedown.metallurgica.infastructure.material.MaterialHelper.getNameForRecipe;

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
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, ItemFlag flag, @NotNull MetallurgicaRegistrate registrate) {
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
