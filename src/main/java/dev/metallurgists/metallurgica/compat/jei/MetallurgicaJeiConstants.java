package dev.metallurgists.metallurgica.compat.jei;

import dev.metallurgists.metallurgica.infastructure.element.Element;
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
