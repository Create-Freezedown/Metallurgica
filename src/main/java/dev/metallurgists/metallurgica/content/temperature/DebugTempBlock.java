package dev.metallurgists.metallurgica.content.temperature;

import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DebugTempBlock extends Block implements IBE<DebugTempBlockEntity> {
    public DebugTempBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<DebugTempBlockEntity> getBlockEntityClass() {
        return DebugTempBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends DebugTempBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.debugTemp.get();
    }
}
