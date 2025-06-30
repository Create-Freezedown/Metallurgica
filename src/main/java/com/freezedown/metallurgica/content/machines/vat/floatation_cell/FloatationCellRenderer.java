package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class FloatationCellRenderer extends SmartBlockEntityRenderer<FloatationCellBlockEntity> {

    public FloatationCellRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(FloatationCellBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (be.getLevel() == null)
            return;
        if (!(be.getLevel().getBlockState(be.getBlockPos().above()).getBlock() instanceof VatBlock))
            return;
        if (be.vatController == null) return;

        BlockState blockState = be.getBlockState();
        VatBlockEntity vatBE = be.vatController;

        PartialModel model = vatBE.getWidth() == 3 ? MetallurgicaPartialModels.largeNozzle : vatBE.getWidth() == 2 ? MetallurgicaPartialModels.mediumNozzle : MetallurgicaPartialModels.smallNozzle;

        float posX = vatBE.getWidth() == 2 ? (float)(vatBE.getController().getX() - be.getBlockPos().getX()) + 0.5F : 0.0F;
        float posZ = vatBE.getWidth() == 2 ? (float)(vatBE.getController().getZ() - be.getBlockPos().getZ()) + 0.5F : 0.0F;
        if (vatBE.getWidth() == 3) {
            posX = (float)(vatBE.getController().getX() - be.getBlockPos().getX()) + 1.0F;
            posZ = (float)(vatBE.getController().getZ() - be.getBlockPos().getZ()) + 1.0F;
        }
        CachedBuffers.partial(model, blockState)
                .light(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above()))
                .translate(posX, 1, posZ).renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
    }
}
