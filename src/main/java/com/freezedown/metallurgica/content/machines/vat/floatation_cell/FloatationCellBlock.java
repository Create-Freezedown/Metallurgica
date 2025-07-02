package com.freezedown.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FloatationCellBlock extends Block implements IBE<FloatationCellBlockEntity> {

    public FloatationCellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<FloatationCellBlockEntity> getBlockEntityClass() {
        return FloatationCellBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FloatationCellBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.floatationCell.get();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return ComparatorUtil.levelOfSmartFluidTank(worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (neighborBlock instanceof VatBlock vat) {
            VatBlockEntity vatBE = vat.getBlockEntity(level, neighborPos);
            if (vatBE != null) {
                withBlockEntityDo(level, pos, blockEntity -> blockEntity.updateVentSize(vatBE.getControllerBE().getWidth(), vatBE.getController()));
            }
        }
    }
}
