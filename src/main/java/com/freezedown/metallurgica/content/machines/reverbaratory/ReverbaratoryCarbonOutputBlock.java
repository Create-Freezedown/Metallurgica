package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ReverbaratoryCarbonOutputBlock extends Block implements IBE<ReverbaratoryCarbonOutputBlockEntity> {
    public ReverbaratoryCarbonOutputBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<ReverbaratoryCarbonOutputBlockEntity> getBlockEntityClass() {
        return ReverbaratoryCarbonOutputBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ReverbaratoryCarbonOutputBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.reverbaratoryCarbonOutput.get();
    }
}
