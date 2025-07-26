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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SheetFlag extends ItemFlag {

    @Getter
    private int pressTimes = 1;

    @Getter
    public boolean needsTransitional = false;

    public SheetFlag(String existingNamespace) {
        super("%s_sheet", existingNamespace);
        this.setTagPatterns(List.of("c:plates", "c:plates/%s"));
    }

    public SheetFlag() {
        this("metallurgica");
    }



    public SheetFlag pressTimes(int pressTimes) {
        this.pressTimes = pressTimes;
        this.needsTransitional = pressTimes > 1;
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
        return FlagKey.SHEET;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (needsTransitional) {
            flags.ensureSet(FlagKey.SEMI_PRESSED_SHEET, true);
        }
        flags.ensureSet(FlagKey.INGOT, true);
    }
}
