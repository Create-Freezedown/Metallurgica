package com.freezedown.metallurgica.foundation.item.registry.flags.item;

import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.item.registry.flags.FlagKey;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ISpecialAssetLocation;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ISpecialLangSuffix;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.ItemFlag;
import com.freezedown.metallurgica.foundation.item.registry.flags.base.MaterialFlags;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuggetFlag extends ItemFlag implements ISpecialAssetLocation, ISpecialLangSuffix {
    @Getter
    private boolean requiresCompacting = false;
    private boolean $shard;
    public NuggetFlag(String existingNamespace, boolean $shard) {
        super($shard ? "%s_shard" :"%s_nugget", existingNamespace);
        this.$shard = $shard;
        String singularTag = $shard ? "c:shards/%s_shard" : "c:nuggets/%s_nugget";
        this.setTagPatterns(List.of($shard? "c:/shards" :"c:nuggets",$shard? "c:/shards/%s" : "c:nuggets/%s", singularTag));
    }

    public NuggetFlag(boolean $shard) {
        this("metallurgica", $shard);
    }

    public NuggetFlag(String existingNamespace) {
        this(existingNamespace, false);
    }

    public NuggetFlag() {
        this("metallurgica", false);
    }


    public NuggetFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
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
    public FlagKey<?> getKey() {
        return FlagKey.NUGGET;
    }

    @Override
    public String getAssetName() {
        return $shard ? "shard" : "nugget";
    }

    @Override
    public String getLangSuffix(){return $shard ? "shard" : "nugget";}

}
