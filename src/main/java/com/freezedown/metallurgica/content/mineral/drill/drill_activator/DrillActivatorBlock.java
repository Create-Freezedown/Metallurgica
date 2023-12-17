package com.freezedown.metallurgica.content.mineral.drill.drill_activator;

import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.registry.MetallurgicaBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillActivatorBlock extends KineticBlock implements IBE<DrillActivatorBlockEntity>, ICogWheel, IWrenchable {
    
    public DrillActivatorBlock(Properties pProperties) {
        super(pProperties);
    }
    
    public static boolean isActivator(BlockState state) {
        return state.getBlock() instanceof DrillActivatorBlock;
    }
    
    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }
    
    @Override
    public Class<DrillActivatorBlockEntity> getBlockEntityClass() {
        return DrillActivatorBlockEntity.class;
    }
    
    @Override
    public BlockEntityType<? extends DrillActivatorBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.drillActivator.get();
    }
}
