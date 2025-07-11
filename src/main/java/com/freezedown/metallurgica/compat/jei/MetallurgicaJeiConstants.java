package com.freezedown.metallurgica.compat.jei;

import com.freezedown.metallurgica.infastructure.element.Element;
import mezz.jei.api.ingredients.IIngredientType;

public class MetallurgicaJeiConstants {

    public static final IIngredientType<Element> ELEMENT = new IIngredientType<>() {

        @Override
        public Class<? extends Element> getIngredientClass() {
            return Element.class;
        }

        public String getUid() {
            return "metallurgica:element";
        }
    };
}
