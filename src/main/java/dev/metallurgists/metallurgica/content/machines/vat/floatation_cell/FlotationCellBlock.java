package dev.metallurgists.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FlotationCellBlock extends Block implements IBE<FlotationCellBlockEntity> {

    public FlotationCellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<FlotationCellBlockEntity> getBlockEntityClass() {
        return FlotationCellBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FlotationCellBlockEntity> getBlockEntityType() {
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
