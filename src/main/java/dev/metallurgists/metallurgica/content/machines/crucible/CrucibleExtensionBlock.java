package dev.metallurgists.metallurgica.content.machines.crucible;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CrucibleExtensionBlock extends HorizontalDirectionalBlock implements IBE<CrucibleExtensionBlockEntity>, IWrenchable {
    
    protected CrucibleExtensionBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<CrucibleExtensionBlockEntity> getBlockEntityClass() {
        return CrucibleExtensionBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends CrucibleExtensionBlockEntity> getBlockEntityType() {
        return null;
    }
}
