package com.freezedown.metallurgica.infastructure.material.registry.flags.item;

import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.foundation.material.item.MaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RubbleFlag extends ItemFlag {
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
    public FlagKey<?> getKey() {
        return FlagKey.RUBBLE;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.MINERAL, true);
    }
}
