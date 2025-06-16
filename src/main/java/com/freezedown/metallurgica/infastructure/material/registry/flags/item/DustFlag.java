package com.freezedown.metallurgica.infastructure.material.registry.flags.item;

import com.freezedown.metallurgica.foundation.material.item.IMaterialItem;
import com.freezedown.metallurgica.foundation.material.item.MaterialItem;
import com.freezedown.metallurgica.infastructure.material.Material;
import com.freezedown.metallurgica.infastructure.material.registry.flags.FlagKey;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DustFlag extends ItemFlag implements ISpecialLangSuffix {

    @Getter
    private boolean powder;

    public DustFlag(String existingNamespace, boolean powder) {
        super(powder ? "%s_powder" : "%s_dust", existingNamespace);
        this.powder = powder;
        List<String> patterns = powder ? List.of("c:powders", "c:powders/%s") : List.of("c:dusts", "c:dusts/%s");
        this.setTagPatterns(patterns);
    }

    public DustFlag() {
        this("metallurgica", false);
    }
    public DustFlag(boolean powder) {
        this("metallurgica", powder);
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
        return FlagKey.DUST;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getLangSuffix() {
        return isPowder() ? "powder" : "dust";
    }
}
