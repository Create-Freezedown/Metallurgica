package com.freezedown.metallurgica.foundation.mixin.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.gui.TooltipRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(TooltipRenderer.class)
public interface TooltipRendererAccessor {
    @Invoker(
            value = "drawHoveringText",
            remap = false
    )
    public static <T> void invokeDrawHoveringText(PoseStack poseStack, List<Component> textLines, int x, int y, ITypedIngredient<T> typedIngredient, Font font) {};
}
