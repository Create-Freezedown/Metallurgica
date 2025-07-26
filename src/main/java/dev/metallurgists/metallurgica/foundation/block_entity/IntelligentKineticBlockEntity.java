package dev.metallurgists.metallurgica.foundation.block_entity;

import dev.metallurgists.metallurgica.foundation.data.advancement.MAdvancement;
import dev.metallurgists.metallurgica.foundation.data.advancement.MAdvancementBehaviour;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class IntelligentKineticBlockEntity extends KineticBlockEntity {
    public IntelligentKineticBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
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
