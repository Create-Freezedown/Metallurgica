package com.freezedown.metallurgica.content.metalworking.forging.hammer;

import com.freezedown.metallurgica.foundation.util.MetalLang;
import com.freezedown.metallurgica.registry.MetallurgicaPackets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.simibubi.create.AllKeys;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.RenderElement;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.theme.Color;
import net.createmod.ponder.enums.PonderGuiTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.*;

public class RadialHammerMenu extends AbstractSimiScreen {
    public static final Map<HammerMode, String> VALID_MODES = new HashMap<>();

    static {
        registerHammerMode(HammerMode.HEAVY_HIT, "Heavy Hit");
        registerHammerMode(HammerMode.UPSET_UP, "Upset (Up)");
        registerHammerMode(HammerMode.UPSET_RIGHT, "Upset (Right)");
        registerHammerMode(HammerMode.UPSET_DOWN, "Upset (Down)");
        registerHammerMode(HammerMode.UPSET_LEFT, "Upset (Left)");
        registerHammerMode(HammerMode.SPLIT, "Split");
    }

    public static void registerHammerMode(HammerMode hammerMode, String label) {
        if (VALID_MODES.containsKey(hammerMode))
            return;

        VALID_MODES.put(hammerMode, label);
    }

    private final ItemStack hammer;
    private final HammerMode currentMode;
    @Nullable
    private final Level level;
    private final List<Map.Entry<HammerMode, String>> hammerModes;
    private final int innerRadius = 50;
    private final int outerRadius = 110;

    private int selectedPropertyIndex = 0;
    private String propertyLabel = "";
    private int ticksOpen;
    private int selectedModeIndex = 0;

    private final RenderElement iconScroll = RenderElement.of(PonderGuiTextures.ICON_SCROLL);
    private final RenderElement iconUp = RenderElement.of(AllIcons.I_PRIORITY_HIGH);
    private final RenderElement iconDown = RenderElement.of(AllIcons.I_PRIORITY_LOW);

    public static Optional<RadialHammerMenu> tryCreateFor(ItemStack hammer, HammerMode currentMode, @Nullable Level level) {

        var modes = VALID_MODES.entrySet().stream().toList();

        if (modes.isEmpty())
            return Optional.empty();

        return Optional.of(new RadialHammerMenu(hammer, currentMode, level, modes));
    }

    private void initForSelectedProperty() {
        Map.Entry<HammerMode, String> entry = hammerModes.get(selectedPropertyIndex);

        cycleAllHammerValues(currentMode, entry.getKey());

        propertyLabel = entry.getValue();
    }

    private void cycleAllHammerValues(HammerMode currentMode, HammerMode property) {
        int offset = 0;
        int safety = 100;
        while (safety-- > 0) {
            if (currentMode.equals(property)) {
                offset = 99 - safety;
                break;
            }
        }

        safety = 100;

        offset = Mth.clamp(offset, 0, VALID_MODES.size() - 1);
        selectedModeIndex = (offset == 0) ? 0 : (VALID_MODES.size() - offset);
    }

    private RadialHammerMenu(ItemStack hammer, HammerMode currentMode, @Nullable Level level, List<Map.Entry<HammerMode, String>> hammerModes) {
        this.hammer = hammer;
        this.currentMode = currentMode;
        this.level = level;
        this.hammerModes = hammerModes;

        initForSelectedProperty();
    }

    @Override
    public void tick() {
        ticksOpen++;
        super.tick();
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        PoseStack ms = graphics.pose();

        ms.pushPose();
        ms.translate(x, y, 0);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int mouseOffsetX = mouseX - this.width / 2;
        int mouseOffsetY = mouseY - this.height / 2;

        if (Mth.length(mouseOffsetX, mouseOffsetY) > innerRadius - 5) {
            double theta = Mth.atan2(mouseOffsetX, mouseOffsetY);

            float sectorSize = 360f / VALID_MODES.size();

            selectedModeIndex = (int) Math.floor(
                    ((-AngleHelper.deg(Mth.atan2(mouseOffsetX, mouseOffsetY)) + 180 + sectorSize / 2) % 360)
                            / sectorSize
            );

            renderDirectionIndicator(graphics, theta);
        }

        renderRadialSectors(graphics);

        UIRenderHelper.streak(graphics, 0, 0, 0, 32, 65, Color.BLACK.setAlpha(0.8f));
        UIRenderHelper.streak(graphics, 180, 0, 0, 32, 65, Color.BLACK.setAlpha(0.8f));

        if (selectedPropertyIndex > 0) {
            iconScroll.at(-14, -46).render(graphics);
            iconUp.at(-1, -46).render(graphics);
            graphics.drawCenteredString(font, hammerModes.get(selectedPropertyIndex - 1).getValue(), 0, -30, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());
        }

        if (selectedPropertyIndex < hammerModes.size() - 1) {
            iconScroll.at(-14, 30).render(graphics);
            iconDown.at(-1, 30).render(graphics);
            graphics.drawCenteredString(font, hammerModes.get(selectedPropertyIndex + 1).getValue(), 0, 22, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());
        }

        graphics.drawCenteredString(font, "Currently", 0, -13, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());
        graphics.drawCenteredString(font, "Changing:", 0, -3, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());
        graphics.drawCenteredString(font, propertyLabel, 0, 7, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());

        ms.popPose();
    }

    private void renderRadialSectors(GuiGraphics graphics) {
        int sectors = VALID_MODES.size();
        if (sectors < 2)
            return;

        PoseStack poseStack = graphics.pose();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return;

        float sectorAngle = 360f / sectors;
        int sectorWidth = outerRadius - innerRadius;

        poseStack.pushPose();

        for (int i = 0; i < sectors; i++) {
            Color innerColor = Color.WHITE.setAlpha(0.05f);
            Color outerColor = Color.WHITE.setAlpha(0.3f);
            HammerMode hammerMode = hammerModes.get(selectedPropertyIndex).getKey();

            poseStack.pushPose();

            if (i == selectedModeIndex) {
                innerColor.mixWith(new Color(0.8f, 0.8f, 0.2f, 0.2f), 0.5f);
                outerColor.mixWith(new Color(0.8f, 0.8f, 0.2f, 0.6f), 0.5f);

                UIRenderHelper.drawRadialSector(graphics, outerRadius + 2, outerRadius + 3, -(sectorAngle / 2 + 90), sectorAngle, outerColor, outerColor);
            }

            UIRenderHelper.drawRadialSector(graphics, innerRadius, outerRadius, -(sectorAngle / 2 + 90), sectorAngle, innerColor, outerColor);
            Color c = innerColor.copy().setAlpha(0.5f);
            UIRenderHelper.drawRadialSector(graphics, innerRadius - 3, innerRadius - 2, -(sectorAngle / 2 + 90), sectorAngle, c, c);

            TransformStack.of(poseStack)
                    .translateY(-(sectorWidth / 2f + innerRadius))
                    .rotateZDegrees(-i * sectorAngle);

            poseStack.translate(0, 0, 100);

            try {
                AllIcons.I_CART_ROTATE.render(graphics, -12, 12);
                //GuiGameElement.of(blockState, blockEntity)
                //        .rotateBlock(player.getXRot(), player.getYRot() + 180, 0f)
                //        .scale(24)
                //        .at(-12, 12)
                //        .render(graphics);
            } catch (Exception e) {
                Create.LOGGER.warn("Failed to render blockstate in RadialWrenchMenu", e);
                selectedModeIndex = 0;
                return;
            }

            poseStack.translate(0, 0, 50);

            if (i == selectedModeIndex) {
                graphics.drawCenteredString(font, hammerMode.getTranslatedName(), 0, 15, UIRenderHelper.COLOR_TEXT.getFirst().getRGB());
            }

            poseStack.popPose();

            poseStack.pushPose();

            TransformStack.of(poseStack)
                    .rotateZDegrees(sectorAngle / 2);

            poseStack.translate(0, -innerRadius - 20, 10);

            UIRenderHelper.angledGradient(graphics, -90, 0, 0, 0.5f, sectorWidth - 10, Color.WHITE.setAlpha(0.5f), Color.WHITE.setAlpha(0.15f));
            UIRenderHelper.angledGradient(graphics,  90, 0, 0, 0.5f, 25              , Color.WHITE.setAlpha(0.5f), Color.WHITE.setAlpha(0.15f));
            poseStack.popPose();

            TransformStack.of(poseStack)
                    .rotateZDegrees(sectorAngle);
        }

        poseStack.popPose();

    }

    private void renderDirectionIndicator(GuiGraphics graphics, double theta) {
        PoseStack poseStack = graphics.pose();

        float r = 0.8f;
        float g = 0.8f;
        float b = 0.8f;

        poseStack.pushPose();
        TransformStack.of(poseStack)
                .rotateZ((float) -theta)
                .translateY(innerRadius + 3)
                .translateZ(15);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f mat = poseStack.last().pose();

        bufferbuilder.vertex(mat, 0, 0, 0).color(r, g, b, 0.75f).endVertex();

        bufferbuilder.vertex(mat, 5, -5, 0).color(r, g, b, 0.4f).endVertex();
        bufferbuilder.vertex(mat, 3, -4.5f, 0).color(r, g, b, 0.4f).endVertex();
        bufferbuilder.vertex(mat, 0, -4.2f, 0).color(r, g, b, 0.4f).endVertex();
        bufferbuilder.vertex(mat, -3, -4.5f, 0).color(r, g, b, 0.4f).endVertex();
        bufferbuilder.vertex(mat, -5, -5, 0).color(r, g, b, 0.4f).endVertex();

        tesselator.end();

        poseStack.popPose();
    }

    private void submitChange() {
        HammerMode selectedMode = hammerModes.get(selectedModeIndex).getKey();
        if (selectedMode != currentMode) {
            MetallurgicaPackets.getChannel().sendToServer(new RadialHammerMenuSubmitPacket(hammer, selectedMode));
        }

        onClose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        Color color = BACKGROUND_COLOR
                .scaleAlpha(Math.min(1, (ticksOpen + AnimationTickHolder.getPartialTicks()) / 20f));

        graphics.fillGradient(0, 0, this.width, this.height, color.getRGB(), color.getRGB());
    }

    @Override
    public boolean keyReleased(int code, int scanCode, int modifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(code, scanCode);
        if (AllKeys.ROTATE_MENU.getKeybind().isActiveAndMatches(mouseKey)) {
            submitChange();
            return true;
        }
        return super.keyReleased(code, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == InputConstants.MOUSE_BUTTON_LEFT) {
            submitChange();
            return true;
        } else if (pButton == InputConstants.MOUSE_BUTTON_RIGHT) {
            onClose();
            return true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (VALID_MODES.size() < 2)
            return super.mouseScrolled(pMouseX, pMouseY, pDelta);

        int indexDelta = (int) Math.round(Math.signum(-pDelta));

        int newIndex = selectedPropertyIndex + indexDelta;
        if (newIndex < 0)
            return false;

        if (newIndex >= VALID_MODES.size())
            return false;

        selectedPropertyIndex = newIndex;
        initForSelectedProperty();

        return true;
    }

    @Override
    public void removed() {
        RadialHammerHandler.COOLDOWN = 2;

        super.removed();
    }


    public enum HammerMode implements StringRepresentable {
        HEAVY_HIT,
        UPSET_UP,
        UPSET_RIGHT,
        UPSET_DOWN,
        UPSET_LEFT,
        SPLIT
        ;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        public Component getTranslatedName() {
            return MetalLang.translate(getSerializedName()).component();
        }
    }
}
