package dev.metallurgists.metallurgica.foundation.material.block.renderer;

import dev.metallurgists.metallurgica.foundation.material.block.MaterialCogWheelBlock;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialCogWheelRenderer extends KineticBlockEntityRenderer<SimpleKineticBlockEntity> {
    public MaterialCogWheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(SimpleKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!AllBlocks.LARGE_COGWHEEL.has(be.getBlockState())) {
            BlockState state = this.getRenderedBlockState(be);
            RenderType type = this.getRenderType(be, state);
            renderRotatingBuffer(be, this.getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
        } else {
            Direction.Axis axis = getRotationAxisOf(be);
            Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
            BlockState state = be.getBlockState();
            if (state.getBlock() instanceof MaterialCogWheelBlock materialGirl) {
                PartialModel model = MetMaterialPartialModels.getPartial(materialGirl.getMaterial(), materialGirl.getFlag().getKey());
                renderRotatingBuffer(be, CachedBuffers.partialFacingVertical(model, be.getBlockState(), facing), ms, buffer.getBuffer(RenderType.cutoutMipped()), light);
                float angle = getAngleForLargeCogShaft(be, axis);
                SuperByteBuffer shaft = CachedBuffers.partialFacingVertical(AllPartialModels.COGWHEEL_SHAFT, be.getBlockState(), facing);
                kineticRotationTransform(shaft, be, axis, angle, light);
                shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            }
        }
    }


    public static float getAngleForLargeCogShaft(SimpleKineticBlockEntity be, Direction.Axis axis) {
        BlockPos pos = be.getBlockPos();
        float offset = getShaftAngleOffset(axis, pos);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = (time * be.getSpeed() * 3.0F / 10.0F + offset) % 360.0F / 180.0F * (float)Math.PI;
        return angle;
    }

    public static float getShaftAngleOffset(Direction.Axis axis, BlockPos pos) {
        float offset = 0.0F;
        double d = (double)(((axis == Direction.Axis.X ? 0 : pos.getX()) + (axis == Direction.Axis.Y ? 0 : pos.getY()) + (axis == Direction.Axis.Z ? 0 : pos.getZ())) % 2);
        if (d == (double)0.0F) {
            offset = 22.5F;
        }

        return offset;
    }
}
