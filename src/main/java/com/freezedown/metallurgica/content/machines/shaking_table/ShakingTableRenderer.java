package com.freezedown.metallurgica.content.machines.shaking_table;

import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ShakingTableRenderer extends KineticBlockEntityRenderer<ShakingTableBlockEntity> {
    
    public ShakingTableRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    protected void renderSafe(ShakingTableBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderItemsOf(be, partialTicks, ms, buffer, light, overlay, be.shakingTableBehaviour);
        renderTable(be, partialTicks, ms, buffer, light);
    }
    
    public float getTableVibration(float partialTicks) {
        return partialTicks * 0.5F / 16.0F;
    }
    public static float getItemVibration(float partialTicks) {
        return partialTicks * 0.25F / 16.0F;
    }
    
    protected void renderTable(ShakingTableBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState blockState = be.getBlockState();
        float offset = 0.0F;
        if (be.haveRecipe()) {
            offset = getTableVibration(partialTicks);
        }
        float constOffset = (float) (1.5 / 16.0F);
        SuperByteBuffer superBuffer = CachedBufferer.partialFacing(MetallurgicaPartialModels.shakerPlatform, blockState, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
        if (blockState.getValue(ShakingTableBlock.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z) {
            if (blockState.getValue(ShakingTableBlock.HORIZONTAL_FACING) == Direction.NORTH) {
                superBuffer.translate(0.0, 0.0, -constOffset);
                superBuffer.translate(0.0, 0.0, -offset);
            } else {
                superBuffer.translate(0.0, 0.0, constOffset);
                superBuffer.translate(0.0, 0.0, offset);
            }
        } else {
            if (blockState.getValue(ShakingTableBlock.HORIZONTAL_FACING) == Direction.WEST) {
                superBuffer.translate(-constOffset, 0.0, 0.0);
                superBuffer.translate(-offset, 0.0, 0.0);
            } else {
                superBuffer.translate(constOffset, 0.0, 0.0);
                superBuffer.translate(offset, 0.0, 0.0);
            }
        }
        superBuffer.color(16777215).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }
    
    public static void renderItemsOf(ShakingTableBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ShakingTableBehaviour shakingTableBehaviour) {
        
        TransportedItemStack transported = shakingTableBehaviour.heldItem;
        TransformStack msr = TransformStack.cast(ms);
        Vec3 itemPosition = VecHelper.getCenterOf(be.getBlockPos());
        float itemOffset = 0.0F;
        if (be.haveRecipe()) {
            itemOffset = getItemVibration(partialTicks);
        }
        ms.pushPose();
        ms.translate(.5f, 13.75 / 16f, .5f);
        ms.translate(itemOffset, 0, itemOffset);
        
        if (transported != null)
            shakingTableBehaviour.incoming.add(transported);
        
        // Render main items
        for (TransportedItemStack tis : shakingTableBehaviour.incoming) {
            ms.pushPose();
            msr.nudge(0);
            float offset = Mth.lerp(partialTicks, tis.prevBeltPosition, tis.beltPosition);
            float sideOffset = Mth.lerp(partialTicks, tis.prevSideOffset, tis.sideOffset);
            
            if (tis.insertedFrom.getAxis()
                    .isHorizontal()) {
                Vec3 offsetVec = Vec3.atLowerCornerOf(tis.insertedFrom.getOpposite()
                        .getNormal()).scale(.5f - offset);
                ms.translate(offsetVec.x, offsetVec.y, offsetVec.z);
                boolean alongX = tis.insertedFrom.getClockWise()
                        .getAxis() == Direction.Axis.X;
                if (!alongX)
                    sideOffset *= -1;
                ms.translate(alongX ? sideOffset : 0, 0, alongX ? 0 : sideOffset);
            }
            
            ItemStack itemStack = tis.stack;
            int angle = tis.angle;
            Random r = new Random(0);
            renderItem(ms, buffer, light, overlay, itemStack, angle, r, itemPosition);
            ms.popPose();
        }
        
        if (transported != null)
            shakingTableBehaviour.incoming.remove(transported);
        
        // Render output items
        for (int i = 0; i < shakingTableBehaviour.processingOutputBuffer.getSlots(); i++) {
            ItemStack stack = shakingTableBehaviour.processingOutputBuffer.getStackInSlot(i);
            if (stack.isEmpty())
                continue;
            ms.pushPose();
            msr.nudge(i);
            
            boolean renderUpright = BeltHelper.isItemUpright(stack);
            msr.rotateY(360 / 8f * i);
            ms.translate(.35f, 0, 0);
            if (renderUpright)
                msr.rotateY(-(360 / 8f * i));
            Random r = new Random(i + 1);
            int angle = (int) (360 * r.nextFloat());
            renderItem(ms, buffer, light, overlay, stack, renderUpright ? angle + 90 : angle, r, itemPosition);
            ms.popPose();
        }
        
        ms.popPose();
    }
    
    public static void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack,
                                  int angle, Random r, Vec3 itemPosition) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        TransformStack msr = TransformStack.cast(ms);
        int count = (int) (Mth.log2((int) (itemStack.getCount()))) / 2;
        boolean renderUpright = BeltHelper.isItemUpright(itemStack);
        boolean blockItem = itemRenderer.getModel(itemStack, null, null, 0)
                .isGui3d();
        
        ms.pushPose();
        msr.rotateY(angle);
        
        if (renderUpright) {
            Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
            if (renderViewEntity != null) {
                Vec3 positionVec = renderViewEntity.position();
                Vec3 vectorForOffset = itemPosition;
                Vec3 diff = vectorForOffset.subtract(positionVec);
                float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
                ms.mulPose(Vector3f.YP.rotation(yRot));
            }
            ms.translate(0, 3 / 32d, -1 / 16f);
        }
        
        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem)
                ms.translate(r.nextFloat() * .0625f * i, 0, r.nextFloat() * .0625f * i);
            ms.scale(.5f, .5f, .5f);
            if (!blockItem && !renderUpright) {
                ms.translate(0, -3 / 16f, 0);
                msr.rotateX(90);
            }
            itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);
            ms.popPose();
            
            if (!renderUpright) {
                if (!blockItem)
                    msr.rotateY(10);
                ms.translate(0, blockItem ? 1 / 64d : 1 / 16d, 0);
            } else
                ms.translate(0, 0, -1 / 16f);
        }
        
        ms.popPose();
    }
    
    @Override
    public boolean shouldRenderOffScreen(ShakingTableBlockEntity be) {
        return true;
    }
    
    @Override
    protected BlockState getRenderedBlockState(ShakingTableBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
