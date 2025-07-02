package com.freezedown.metallurgica.content.mineral.deposit;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MineralDepositBlock extends Block implements IBE<MineralDepositBlockEntity> {
    public MineralDepositBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public Class<MineralDepositBlockEntity> getBlockEntityClass() {
        return MineralDepositBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends MineralDepositBlockEntity> getBlockEntityType() {
        return null;//MetallurgicaBlockEntities.mineralDeposit.get();
    }
}
