package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MineralFlag extends ItemFlag implements ISpecialLangSuffix {

    @Getter
    private boolean $native;

    public MineralFlag(String existingNamespace, boolean $native) {
        super($native ? "native_%s" : "%s", existingNamespace);
        this.$native = $native;
        String singularTag = $native ? "c:raw_materials/native_%s" : "c:raw_materials/%s";
        this.setTagPatterns(List.of("c:raw_materials", singularTag));
    }

    public MineralFlag() {
        this("metallurgica", false);
    }

    public MineralFlag(boolean $native) {
        this("metallurgica", $native);
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
        return FlagKey.MINERAL;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getLangSuffix() {
        return is$native() ? "native_mineral" : "mineral";
    }
}
