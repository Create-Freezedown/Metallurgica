package com.freezedown.metallurgica.experimental.shader;

import com.freezedown.metallurgica.foundation.mixin.accessor.PostChainAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.joml.Matrix4f;

public class RenderUtil {
    private static final Minecraft minecraft = Minecraft.getInstance();
    
    public static void updateUniform(String name, float value, PostChain postChain) {
        for (var pass : ((PostChainAccessor) postChain).getPasses()) {
            pass.getEffect().safeGetUniform(name).set(value);
        }
    }
    
    public static void updateUniform(String name, float[] value, PostChain postChain) {
        for (var pass : ((PostChainAccessor) postChain).getPasses()) {
            pass.getEffect().safeGetUniform(name).set(value);
        }
    }
    
    public static void blit(PoseStack poseStack, float x, float y, float width, float height) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        blit(poseStack.last().pose(), x, y, x + width, y + height, 0.0F, 1.0F, 1.0F, 0.0F);
    }
    
    public static void blit(PoseStack poseStack, float x, float y, float x2, float y2,
                            float textureX, float textureY, float textureX2, float textureY2, float width, float height) {
        float u = textureX / width;
        float u2 = textureX2 / width;
        float v = textureY / height;
        float v2 = textureY2 / height;
        blit(poseStack.last().pose(), x, y, x2, y2, u, v, u2, v2);
    }
    
    public static void blit(Matrix4f matrix, float x, float y, float x2, float y2,
                            float u, float v, float u2, float v2) {
        final var tesselator = Tesselator.getInstance();
        final var builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(matrix, x, y2, 0.0F).uv(u, v).endVertex();
        builder.vertex(matrix, x2, y2, 0.0F).uv(u2, v).endVertex();
        builder.vertex(matrix, x2, y, 0.0F).uv(u2, v2).endVertex();
        builder.vertex(matrix, x, y, 0.0F).uv(u, v2).endVertex();
        tesselator.end();
    }
}
