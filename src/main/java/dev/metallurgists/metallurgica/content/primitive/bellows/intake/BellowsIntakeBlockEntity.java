package dev.metallurgists.metallurgica.content.primitive.bellows.intake;

import dev.metallurgists.metallurgica.foundation.block_entity.behaviour.DisplayStateBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BellowsIntakeBlockEntity extends SmartBlockEntity {

    @Getter
    DisplayStateBehaviour displayStateBehaviour;

    public BellowsIntakeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(displayStateBehaviour = new DisplayStateBehaviour(this));
    }

    public void setDisplayState(BlockState state) {
        if (displayStateBehaviour != null) displayStateBehaviour.setDisplayState(state);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }
}
