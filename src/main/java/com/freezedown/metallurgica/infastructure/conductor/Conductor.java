package com.freezedown.metallurgica.infastructure.conductor;

import com.freezedown.metallurgica.infastructure.IHasDescriptionId;
import com.freezedown.metallurgica.registry.misc.MetallurgicaRegistries;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;


@Getter
public class Conductor implements IHasDescriptionId {
    private String descriptionId;

    private final float resistivity;
    private int[] color1 = {193, 90, 54, 255};
    private int[] color2 = {156, 78, 49, 255};

    public Conductor(float resistivity) {
        this.resistivity = resistivity;
    }

    public Conductor(float resistivity, int[] color1, int[] color2) {
        this.resistivity = resistivity;
        this.color1 = color1;
        this.color2 = color2;
    }

    public String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            ResourceLocation key = MetallurgicaRegistries.CONDUCTOR.getKey(this);
            this.descriptionId = Util.makeDescriptionId("conductor", key);
        }

        return this.descriptionId;
    }


    public static void renderWire(Conductor conductor, PoseStack pMatrixStack, MultiBufferSource pBuffer, BlockPos pos1, BlockPos pos2, float offsetX1, float offsetY1, float offsetZ1, float offsetX2, float offsetY2, float offsetZ2, float curve) {
        pMatrixStack.pushPose();
        Vec3 vec3 = new Vec3((double)0.0F, (double)0.0F, (double)0.0F);
        BlockPos pos2Local = pos1.subtract(pos2);
        pMatrixStack.translate((double)0.5F + (double)offsetX1, (double)0.5F + (double)offsetY1, (double)0.5F + (double)offsetZ1);
        vec3 = vec3.add((double)((float)pos2Local.getX() - offsetX1 + offsetX2) + 0.01, (double)((float)pos2Local.getY() - offsetY1 + offsetY2), (double)((float)pos2Local.getZ() - offsetZ1 + offsetZ2) + 0.01);
        float f = (float)vec3.x;
        float f1 = (float)vec3.y;
        float f2 = (float)vec3.z;
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = (float)(Mth.fastInvSqrt((double)(f * f + f2 * f2)) * (double)0.025F / (double)2.0F);
        float f5 = f2 * f4;
        float f6 = f * f4;
        int i = 15;
        int j = 15;
        int k = 15;
        int l = 15;

        for(int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(conductor, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.03F, 0.03F, f5, f6, i1, false, curve);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(conductor, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.03F, 0.0F, f5, f6, j1, true, curve);
        }

        pMatrixStack.popPose();
    }

    private static void addVertexPair(Conductor conductor, VertexConsumer vertexConsumer, Matrix4f matrix4f, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float thickness, float p_174318_, float p_174319_, float p_174320_, int value, boolean p_174322_, float curve) {
        float f = (float)value / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        int[] color = (value % 2 == 0) ? conductor.getColor1() : conductor.getColor2();
        float f1 = value % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        int r = color[0];
        int g = color[1];
        int b = color[2];
        int a = color[3];
        float x = p_174310_ * f;
        float pain = ((float)value * curve * 24.0F - (float)(value * value) * curve) * -1.0F;
        float y = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float z = p_174312_ * f;
        vertexConsumer.vertex(matrix4f, x - p_174319_, y + p_174318_ + pain, z + p_174320_).color(r, g, b, a).uv2(k).endVertex();
        vertexConsumer.vertex(matrix4f, x + p_174319_, y + thickness - p_174318_ + pain, z - p_174320_).color(r, g, b, a).uv2(k).endVertex();
    }
}
