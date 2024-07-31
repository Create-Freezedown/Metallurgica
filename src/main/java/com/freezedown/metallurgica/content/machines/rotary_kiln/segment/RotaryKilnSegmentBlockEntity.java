package com.freezedown.metallurgica.content.machines.rotary_kiln.segment;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class RotaryKilnSegmentBlockEntity extends KineticBlockEntity {
    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;
    
    public RotaryKilnSegmentBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    
    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if (clientPacket)
            visualSpeed.chase(getGeneratedSpeed(), 1 / 64f, LerpedFloat.Chaser.EXP);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!level.isClientSide)
            return;
        
        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 3 / 10f;
        angle %= 360;
    }
}
