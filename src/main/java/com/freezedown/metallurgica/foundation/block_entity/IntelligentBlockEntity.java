package com.freezedown.metallurgica.foundation.block_entity;

import com.freezedown.metallurgica.foundation.data.advancement.MAdvancement;
import com.freezedown.metallurgica.foundation.data.advancement.MAdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class IntelligentBlockEntity extends SmartBlockEntity {
    public IntelligentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public void registerAwardables(List<BlockEntityBehaviour> behaviours, MAdvancement... advancements) {
        for (BlockEntityBehaviour behaviour : behaviours) {
            if (behaviour instanceof MAdvancementBehaviour ab) {
                ab.add(advancements);
                return;
            }
        }
        behaviours.add(new MAdvancementBehaviour(this, advancements));
    }
    
    public void award(MAdvancement advancement) {
        MAdvancementBehaviour behaviour = getBehaviour(MAdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.awardPlayer(advancement);
    }
    
    public void awardIfNear(MAdvancement advancement, int range) {
        MAdvancementBehaviour behaviour = getBehaviour(MAdvancementBehaviour.TYPE);
        if (behaviour != null)
            behaviour.awardPlayerIfNear(advancement, range);
    }
}
