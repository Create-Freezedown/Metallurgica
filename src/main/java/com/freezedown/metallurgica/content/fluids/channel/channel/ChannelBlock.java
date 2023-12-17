package com.freezedown.metallurgica.content.fluids.channel.channel;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ChannelBlock extends Block implements IWrenchable, IBE<ChannelBlockEntity> {
    public ChannelBlock(BlockBehaviour.Properties p_49795_) {
        super(p_49795_);
    }
    
    public Class<ChannelBlockEntity> getBlockEntityClass() {
        return ChannelBlockEntity.class;
    }
    
    public BlockEntityType<? extends ChannelBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.channel.get();
    }
}
