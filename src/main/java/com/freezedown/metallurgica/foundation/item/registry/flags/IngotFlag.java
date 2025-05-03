package com.freezedown.metallurgica.foundation.item.registry.flags;

import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.NoRegisterFactoryFlag;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import lombok.Getter;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class IngotFlag extends ItemFlag {
    @Getter
    private boolean requiresCompressing;

    public IngotFlag() {
        super("%s_ingot");
    }

    public IngotFlag(String existingNamespace) {
        super("%s_ingot", existingNamespace);
    }

    public IngotFlag requiresCompressing(boolean requiresCompressing) {
        this.requiresCompressing = requiresCompressing;
        return  this;
    }

    @Override
    public ItemEntry<? extends MaterialItem> registerItem(@NotNull Material material, ItemFlag flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
