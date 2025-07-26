package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GemFlag extends ItemFlag {


    public GemFlag(String existingNamespace) {
        super("%s_gem", existingNamespace);
        this.setTagPatterns(List.of("c:gems", "c:gems/%s"));
    }

    public GemFlag() {
        this("metallurgica");
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
        return FlagKey.GEM;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
