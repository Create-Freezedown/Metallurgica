package com.freezedown.metallurgica.content.machines.blast_furnace.tuyere;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class TuyereBlock extends Block implements IWrenchable, IBE<TuyereBlockEntity> {
    public TuyereBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<TuyereBlockEntity> getBlockEntityClass() {
        return TuyereBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends TuyereBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.tuyere.get();
    }
}
