package dev.metallurgists.metallurgica.compat.jei.custom.element;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import lombok.RequiredArgsConstructor;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("removal")
@ParametersAreNonnullByDefault
@RequiredArgsConstructor
public class ElementIngredientRenderer implements IIngredientRenderer<Element> {
    private final int size;

    @Override
    public int getWidth() {
        return size;
    }

    @Override
    public int getHeight() {
        return size;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Element element) {
        render(guiGraphics, element, 0, 0);
    }

    @Override
    public void render(GuiGraphics guiGraphics, @NotNull Element element, int posX, int posY) {
        if (element != null) {
            RenderSystem.enableBlend();

            drawFluid(guiGraphics, 16, 16, element, posX, posY);

            RenderSystem.setShaderColor(1, 1, 1, 1);

            RenderSystem.disableBlend();
        }
    }

    private void drawFluid(GuiGraphics guiGraphics, final int width, final int height, Element element, int posX, int posY) {
        getStillFluidSprite().ifPresent(fluidStillSprite -> {
                    int fluidColor = new Color(element.getColor(), true).getRGB();

                    long amount = 1000;
                    long scaledAmount = (amount * height) / 1000;
                    if (amount > 0 && scaledAmount < 1) {
                        scaledAmount = 1;
                    }
                    if (scaledAmount > height) {
                        scaledAmount = height;
                    }

                    drawTiledSprite(guiGraphics, width, height, fluidColor, scaledAmount, fluidStillSprite, posX, posY);
                });
    }

    public Optional<TextureAtlasSprite> getStillFluidSprite() {
        return Optional.ofNullable(Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(Metallurgica.asResource("fluid/thin_fluid_still")))
                .filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    private static void drawTiledSprite(GuiGraphics guiGraphics, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite, int posX, int posY) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / 16;
        final int xRemainder = tiledWidth - (xTileCount * 16);
        final long yTileCount = scaledAmount / 16;
        final long yRemainder = scaledAmount - (yTileCount * 16);

        final int yStart = tiledHeight + posY;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : 16;
                long height = (yTile == yTileCount) ? yRemainder : 16;
                int x = posX + (xTile * 16);
                int y = yStart - ((yTile + 1) * 16);
                if (width > 0 && height > 0) {
                    long maskTop = 16 - height;
                    int maskRight = 16 - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }

    @Override
    public @NotNull List<Component> getTooltip(Element element, TooltipFlag flag) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(element.getDisplayName());
        if (flag.isAdvanced()) {
            tooltip.add((Component.literal(element.getKey().toString())).withStyle(ChatFormatting.DARK_GRAY));
        }
        return tooltip;
    }
}
