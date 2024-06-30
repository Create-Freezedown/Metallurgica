package com.freezedown.metallurgica.content.machines.reverbaratory;

import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ReverbaratoryOutputBlock extends Block implements IBE<ReverbaratoryOutputBlockEntity> {
    public static final EnumProperty<ReverbaratoryOutputType> TYPE = EnumProperty.create("type", ReverbaratoryOutputType.class);
    public ReverbaratoryOutputBlock(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
        super.createBlockStateDefinition(builder);
    }
    
    @Override
    public Class<ReverbaratoryOutputBlockEntity> getBlockEntityClass() {
        return ReverbaratoryOutputBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends ReverbaratoryOutputBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.reverbaratoryOutput.get();
    }
}
