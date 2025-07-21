package com.freezedown.metallurgica.foundation.mixin.create.block;

import com.freezedown.metallurgica.foundation.block_entity.behaviour.TankLiningBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlock;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = SpoutBlock.class, remap = false)
public abstract class SpoutBlockMixin extends Block implements IBE<SpoutBlockEntity> {

    public SpoutBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        withBlockEntityDo(world, pos, be -> be.getBehaviour(TankLiningBehaviour.TYPE).destroy());
    }
}
