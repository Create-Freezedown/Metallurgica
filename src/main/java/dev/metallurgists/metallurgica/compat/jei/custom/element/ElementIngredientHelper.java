package dev.metallurgists.metallurgica.compat.jei.custom.element;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.jei.MetallurgicaJeiConstants;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ElementIngredientHelper implements IIngredientHelper<Element> {

    private final IColorHelper colorHelper;

    public ElementIngredientHelper(IColorHelper colorHelper) {
        this.colorHelper = colorHelper;
    }

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

    @Override
    public Iterable<Integer> getColors(Element element) {
        return getStillFluidSprite()
                .map(fluidStillSprite -> {
                    int renderColor = new Color(element.getColor(), true).getRGB();
                    return colorHelper.getColors(fluidStillSprite, renderColor, 1);
                })
                .orElseGet(List::of);
    }

    public Optional<TextureAtlasSprite> getStillFluidSprite() {
        return Optional.ofNullable(Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(Metallurgica.asResource("fluid/thin_fluid_still")))
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }
}
