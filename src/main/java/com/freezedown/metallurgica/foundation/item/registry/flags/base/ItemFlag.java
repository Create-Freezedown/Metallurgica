package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.block.MaterialBlock;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.foundation.item.MaterialItem;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ItemFlag implements IMaterialFlag {

    private final String idPattern;
    private String existingNamespace = "metallurgica";

    public ItemFlag(String idPattern) {
        this.idPattern = idPattern;
    }

    public ItemFlag(String idPattern, String existingNamespace) {
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public String getIdPattern() {
        return idPattern;
    }

    public String getExistingNamespace() {
        return existingNamespace;
    }

    public String getUnlocalizedName() {
        return "materialflag." + MetallurgicaModels.getFlagName(idPattern);
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getLocalizedName());
    }

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModid(), this.idPattern.formatted(material.getName()));
        if (TextUtil.langExists(matSpecificKey)) {
            return matSpecificKey;
        }

        return getUnlocalizedName();
    }

    public abstract ItemEntry<? extends MaterialItem> registerItem(@NotNull Material material, ItemFlag flag, @NotNull MetallurgicaRegistrate registrate);

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
