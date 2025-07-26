package dev.metallurgists.metallurgica.foundation.mixin.tfmg;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineRenderer;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialSpoolItem;
import dev.metallurgists.metallurgica.foundation.mixin.accessor.WindingMachineBlockEntityAccessor;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialPartialModels;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WindingMachineRenderer.class, remap = false)
public abstract class WindingMachineRendererMixin extends KineticBlockEntityRenderer<WindingMachineBlockEntity> {

    public WindingMachineRendererMixin(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Unique WindingMachineBlockEntity metallurgica$blockEntity;

    @Inject(method = "renderSafe(Lcom/drmangotea/tfmg/content/machinery/misc/winding_machine/WindingMachineBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"))
    public void metallurgica$renderSafe(WindingMachineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay, CallbackInfo ci) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());
        metallurgica$blockEntity = be;
        float angle = ((WindingMachineBlockEntityAccessor) be).getAngle();
        if (!be.spool.isEmpty()) {
            if (be.spool.getItem() instanceof MaterialSpoolItem materialSpoolItem) {
                PartialModel model = MetMaterialPartialModels.getPartial(materialSpoolItem.getMaterial(), materialSpoolItem.getFlag().getKey());
                CachedBuffers.partial(model, blockState).light(light).center().rotateYDegrees(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? Math.abs(blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot() - 180.0F) : blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot()).translateZ(-0.4F).translateY(0.4F).rotateXDegrees(angle).uncenter().renderInto(ms, vb);
                if (!be.inventory.isEmpty()) {
                    CachedBuffers.partial(be.getSpeed() != 0.0F ? TFMGPartialModels.CONNNECTING_WIRE_ANIMATED : TFMGPartialModels.CONNNECTING_WIRE, blockState).light(light).center().rotateYDegrees(blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? Math.abs(blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot() - 180.0F) : blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot()).translateY(0.4F).translateZ(0.1F).color(be.spool.getBarColor()).rotateXDegrees(12.0F).uncenter().renderInto(ms, vb);
                }
            }
        }
    }

    @ModifyExpressionValue(method = "renderSafe(Lcom/drmangotea/tfmg/content/machinery/misc/winding_machine/WindingMachineBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"
    ))
    public boolean metallurgica$ignoreSpoolIfMaterial(boolean original) {
        return original && metallurgica$blockEntity.spool.getItem() instanceof MaterialSpoolItem;
    }
}
