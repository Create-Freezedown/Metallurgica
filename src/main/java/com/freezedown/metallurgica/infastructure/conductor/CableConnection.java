package com.freezedown.metallurgica.infastructure.conductor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class CableConnection {
    public final Conductor conductor;
    public final ItemStack cableItem;
    public final float length;
    public final double resistance;
    public static final float CABLE_THICKNESS = 1.5F;
    public final BlockPos point1;
    public final BlockPos point2;
    public final boolean neighborConnection;
    public final boolean shouldRender;

    public CableConnection(Conductor conductor, ItemStack cableItem, float length, BlockPos point1, BlockPos point2, boolean render, boolean neighborConnection) {
        this.conductor = conductor;
        this.cableItem = cableItem;
        this.length = length;
        this.point1 = point1;
        this.point2 = point2;
        this.shouldRender = render;
        this.neighborConnection = neighborConnection;
        this.resistance = WireConductorValues.getResistivity(conductor) * (length / 1.5F);
    }

    public void saveConnection(CompoundTag compound, int value) {
        compound.putInt("X1" + value, this.point1.getX());
        compound.putInt("Y1" + value, this.point1.getY());
        compound.putInt("Z1" + value, this.point1.getZ());
        compound.putInt("X2" + value, this.point2.getX());
        compound.putInt("Y2" + value, this.point2.getY());
        compound.putInt("Z2" + value, this.point2.getZ());
        compound.putFloat("Length" + value, this.length);
        compound.putBoolean("NeighborConnection" + value, this.neighborConnection);
        compound.putBoolean("ShouldRender" + value, this.shouldRender);
        CompoundTag itemTag = compound.getCompound("CableItem");
        this.cableItem.save(itemTag);
        NBTHelper.writeResourceLocation(compound, "Conductor" + value, this.conductor.getKey());
    }

    public void renderWire(PoseStack pMatrixStack, MultiBufferSource pBuffer, float offsetX1, float offsetY1, float offsetZ1, float offsetX2, float offsetY2, float offsetZ2, float curve) {
        pMatrixStack.pushPose();
        Vec3 vec3 = new Vec3((double)0.0F, (double)0.0F, (double)0.0F);
        BlockPos pos2Local = point1.subtract(point2);
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
