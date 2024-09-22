package com.freezedown.metallurgica.content.primitive.log_pile.charred_pile;

import com.freezedown.metallurgica.content.primitive.log_pile.IgnitableLogPileBlock;
import com.freezedown.metallurgica.content.primitive.log_pile.LogPileBlock;
import com.freezedown.metallurgica.foundation.block_entity.IntelligentBlockEntity;
import com.freezedown.metallurgica.foundation.config.MetallurgicaConfigs;
import com.freezedown.metallurgica.foundation.data.advancement.MetallurgicaAdvancements;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import com.freezedown.metallurgica.registry.MetallurgicaTags;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CharredLogPileBlockEntity extends IntelligentBlockEntity {
    private int burnTime;
    private int destructionTime;
    private int coveredFaces;
    public CharredLogPileBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        burnTime = -1;
        destructionTime = -1;
        coveredFaces = 0;
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        registerAwardables(behaviours, MetallurgicaAdvancements.INEFFICIENT_CHARCOAL);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        
        if (this.burnTime == -1) {
            burnTime = MetallurgicaConfigs.server().machineConfig.logPileConfig.logPileBurnTime.get();
        }
        if (this.destructionTime == -1) {
            destructionTime = MetallurgicaConfigs.server().machineConfig.logPileConfig.logPileDestructionTime.get();
        }
        
        if (burnTime > 0) {
            burnTime--;
        } else {
            if (this.burnTime != -1) {
                awardIfNear(MetallurgicaAdvancements.INEFFICIENT_CHARCOAL, 32);
                convertToAshedPile();
            }
        }
        
        if (this.level.canSeeSky(this.worldPosition) || exposedToAir()) {
            if (destructionTime > 0) {
                if (level.isRainingAt(this.worldPosition) && destructionTime - 2 >= 0) destructionTime -= 2; else destructionTime--;
            } else {
                if (this.destructionTime != -1) {
                    shrinkStackHeight();
                    destructionTime = MetallurgicaConfigs.server().machineConfig.logPileConfig.logPileDestructionTime.get();
                }
            }
        }
        convertNeighbors();
    }
    
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        burnTime = compound.getInt("BurnTime");
        destructionTime = compound.getInt("DestructionTime");
        coveredFaces = compound.getInt("CoveredFaces");
        super.read(compound, clientPacket);
    }
    
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("BurnTime", burnTime);
        compound.putInt("DestructionTime", destructionTime);
        compound.putInt("CoveredFaces", coveredFaces);
        super.write(compound, clientPacket);
    }
    
    public void shrinkStackHeight() {
        if (this.level == null) {
            return;
        }
        int layers = this.getBlockState().getValue(CharredLogPileBlock.LAYERS);
        int toSubtract = layers > 1 ? 1 : 0;
        BlockState aboveState = this.level.getBlockState(this.worldPosition.above());
        
        if (aboveState.getBlock() instanceof CharredLogPileBlock) {
            layers = aboveState.getValue(CharredLogPileBlock.LAYERS);
            if (layers > 1) {
                this.level.setBlock(this.worldPosition.above(), MetallurgicaBlocks.charredLogPile.get().defaultBlockState().setValue(CharredLogPileBlock.LAYERS, layers - toSubtract), 3);
            } else {
                this.level.destroyBlock(this.worldPosition.above(), false);
            }
        } else {
            if (layers > 1) {
                this.level.setBlock(this.worldPosition, this.getBlockState().setValue(CharredLogPileBlock.LAYERS, layers - 1), 3);
            } else {
                this.level.destroyBlock(this.worldPosition, false);
            }
        }
    }
    
    public void convertToAshedPile() {
        if (this.level == null) {
            return;
        }
        int layers = this.getBlockState().getValue(CharredLogPileBlock.LAYERS);
        int toSubtract = layers > 1 ? 1 : 0;
        BlockState aboveState = this.level.getBlockState(this.worldPosition.above());
        if (aboveState.getBlock() instanceof CharredLogPileBlock) {
            layers = aboveState.getValue(CharredLogPileBlock.LAYERS);
            this.level.setBlock(this.worldPosition.above(), MetallurgicaBlocks.ashedCharcoalPile.get().defaultBlockState().setValue(LogPileBlock.LAYERS, layers - toSubtract), 3);
        } else {
            this.level.setBlock(this.worldPosition, MetallurgicaBlocks.ashedCharcoalPile.get().defaultBlockState().setValue(LogPileBlock.LAYERS, layers - toSubtract), 3);
        }
    }
    
    public boolean exposedToAir() {
        if (this.level == null) {
            return false;
        }
        // needs all 5 faces to be covered, ignoring the bottom face
        //Corners are not checked
        int coveredFaces = 0;
        AirExposure airExposure = MetallurgicaConfigs.server().machineConfig.logPileConfig.airExposure.get();
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    if (x != 0 && y > 0 && z != 0) {
                        continue;
                    }
                    if (x == z) {
                        continue;
                    }
                    BlockPos neighborPos = this.worldPosition.offset(x, y, z);
                    if (this.level.isEmptyBlock(neighborPos)) {
                        coveredFaces++;
                    }
                    if (airExposure == AirExposure.TAG_SPECIFIC) {
                        BlockState neighborState = this.level.getBlockState(neighborPos);
                        if (neighborState.is(MetallurgicaTags.AllBlockTags.AIR_BLOCKING.tag) && !neighborState.is(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)) {
                            coveredFaces++;
                        }
                    } else if (airExposure == AirExposure.ANY_BLOCK) {
                        BlockState neighborState = this.level.getBlockState(neighborPos);
                        if (neighborState.isCollisionShapeFullBlock(this.level, neighborPos) && !neighborState.is(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)) {
                            coveredFaces++;
                        }
                    }
                }
            }
        }
        this.coveredFaces = coveredFaces;
        return coveredFaces < 5;
    }
    
    public void convertNeighbors() {
        if (this.level == null) {
            return;
        }
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos neighborPos = this.worldPosition.offset(x, y, z);
                    BlockState neighborState = this.level.getBlockState(neighborPos);
                    if (neighborState.getBlock() instanceof IgnitableLogPileBlock) {
                        if (!level.canSeeSky(neighborPos)) {
                            int layers = neighborState.getValue(LogPileBlock.LAYERS);
                            this.level.setBlock(neighborPos, MetallurgicaBlocks.charredLogPile.get().defaultBlockState().setValue(LogPileBlock.LAYERS, layers), 3);
                        }
                    }
                }
            }
        }
    }
    
    public enum AirExposure {
        ANY_BLOCK,
        TAG_SPECIFIC,
    }
    
}
