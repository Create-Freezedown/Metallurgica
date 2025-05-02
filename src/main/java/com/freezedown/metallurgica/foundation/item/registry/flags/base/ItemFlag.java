package com.freezedown.metallurgica.foundation.item.registry.flags.base;

import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import com.freezedown.metallurgica.foundation.item.registry.Material;
import com.freezedown.metallurgica.foundation.util.TextUtil;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class ItemFlag implements IMaterialFlag {

    private final NonNullFunction<Item.Properties, ? extends Item> factory;
    private final String idPattern;
    private String existingNamespace = "metallurgica";

    public ItemFlag(NonNullFunction<Item.Properties, ? extends Item> factory, String idPattern) {
        this.factory = factory;
        this.idPattern = idPattern;
    }

    public ItemFlag(String idPattern, String existingNamespace) {
        this.factory = null;
        this.idPattern = idPattern;
        this.existingNamespace = existingNamespace;
    }

    public NonNullFunction<Item.Properties, ? extends Item> getFactory() {
        return factory;
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

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }
}
