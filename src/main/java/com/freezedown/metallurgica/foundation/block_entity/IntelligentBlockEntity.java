package com.freezedown.metallurgica.foundation.block_entity;

import com.freezedown.metallurgica.foundation.data.advancement.MAdvancement;
import com.freezedown.metallurgica.foundation.data.advancement.MAdvancementBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
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

    public void markForBlockUpdate()
    {
        if (level != null)
        {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
        }
    }

    public void markForSync()
    {
        sendVanillaUpdatePacket();
        setChanged();
    }

    @SuppressWarnings("deprecation")
    public void markDirty()
    {
        if (level != null && level.hasChunkAt(worldPosition))
        {
            level.getChunkAt(worldPosition).setUnsaved(true);
        }
    }

    public void sendVanillaUpdatePacket()
    {
        final ClientboundBlockEntityDataPacket packet = getUpdatePacket();
        final BlockPos pos = getBlockPos();
        if (packet != null && level instanceof ServerLevel serverLevel)
        {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(e -> e.connection.send(packet));
        }
    }
}
