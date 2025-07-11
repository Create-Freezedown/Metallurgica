package com.freezedown.metallurgica.compat.jei.custom.element;

import com.freezedown.metallurgica.compat.jei.MetallurgicaJeiConstants;
import com.freezedown.metallurgica.infastructure.element.Element;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ElementIngredientHelper implements IIngredientHelper<Element> {
    @Override
    public IIngredientType<Element> getIngredientType() {
        return MetallurgicaJeiConstants.ELEMENT;
    }

    @Override
    public String getDisplayName(Element element) {
        return element.getDisplayName().getString();
    }

    @Override
    public String getUniqueId(Element element, UidContext context) {
        return getResourceLocation(element).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(Element element) {
        return element.getKey();
    }

    @Override
    public Element copyIngredient(Element element) {
        return element;
    }

    @Override
    public String getErrorInfo(@Nullable Element element) {
        if (element == null) {
            return "null";
        }
        return getResourceLocation(element).toString();
    }
}
