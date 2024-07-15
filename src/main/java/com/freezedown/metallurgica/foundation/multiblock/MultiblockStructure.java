package com.freezedown.metallurgica.foundation.multiblock;

import com.freezedown.metallurgica.Metallurgica;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jgrapht.alg.util.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.freezedown.metallurgica.foundation.util.ClientUtil.createCustomCubeParticles;

public class MultiblockStructure {
    private final BlockEntity master;
    private final ArrayList<Map<BlockPos, BlockState>> structure;
    
    public MultiblockStructure(BlockEntity master, ArrayList<Map<BlockPos, BlockState>> structure) {
        this.master = master;
        this.structure = structure;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static CuboidBuilder cuboidBuilder(BlockEntity master) {
        return new CuboidBuilder(master);
    }
    
    public static class Builder {
        private final ArrayList<Map<BlockPos, BlockState>> structure = new ArrayList<>();
        
        public Builder addBlock(BlockPos pos, BlockState block) {
            structure.add(Map.of(pos, block));
            return this;
        }
        
        public Builder addBlock(Map<BlockPos, BlockState> block) {
            structure.add(block);
            return this;
        }
        
        public Builder addBlocks(List<Pair<BlockPos, BlockState>> positionsAndStates) {
            for (Pair<BlockPos, BlockState> pair : positionsAndStates) {
                structure.add(Map.of(pair.getFirst(), pair.getSecond()));
            }
            return this;
        }
        
        public MultiblockStructure build(BlockEntity master) {
            return new MultiblockStructure(master, structure);
        }
    }
    
    public static class CuboidBuilder {
        private final BlockEntity master;
        private final ArrayList<Map<BlockPos, BlockState>> structure = new ArrayList<>();
        private int width = 0;
        private int height = 0;
        private int depth = 0;
        
        public CuboidBuilder(BlockEntity master) {
            this.master = master;
        }
        
        public BlockEntity getMaster() {
            return master;
        }
        
        public BlockPos getMasterPosition() {
            return getMaster().getBlockPos();
        }
        
        public BlockState getMasterBlockState() {
            if (getMaster().getLevel() == null) {
                Metallurgica.LOGGER.error("Level is null, cannot get block state");
                return null;
            }
            return getMaster().getLevel().getBlockState(getMasterPosition());
        }
        
        public Direction getMasterDirection() {
            if (getMasterBlockState() == null) {
                Metallurgica.LOGGER.error("Block state is null, cannot get direction");
                return null;
            }
            return getMasterBlockState().getValue(BlockStateProperties.FACING);
        }
        
        public CuboidBuilder cube(int size) {
            return withSize(size, size, size);
        }
        
        public CuboidBuilder withSize(int width, int height) {
            return withSize(width, height, width);
        }
        
        public CuboidBuilder withSize(int width, int height, int depth) {
            this.width = width;
            this.height = height;
            this.depth = depth;
            return this;
        }
        
        public Triple<Integer, Integer, Integer> getSize() {
            return Triple.of(width, height, depth);
        }
        
        public int getWidth() {
            return getSize().getFirst();
        }
        
        public int getHeight() {
            return getSize().getSecond();
        }
        
        public int getDepth() {
            return getSize().getThird();
        }
        
        public CuboidBuilder withBlockAt(int x, int y, int z, BlockState block) {
            BlockPos pos = translateToMaster(x, y, z);
            structure.add(Map.of(pos, block));
            return this;
        }
        
        private BlockPos translateToMaster(int x, int y, int z) {
            BlockPos masterPos = getMasterPosition();
            Direction direction = getMasterDirection();
            if (direction == null) {
                Metallurgica.LOGGER.error("Direction is null, cannot translate position");
                return masterPos;
            }
            
            return masterPos.relative(direction, x).above(y).relative(direction.getClockWise(), z);
        }
        
        public MultiblockStructure build() {
            return new MultiblockStructure(getMaster(), structure);
        }
    }
    
    public ArrayList<Map<BlockPos, BlockState>> getStructure() {
        return structure;
    }
    
    public BlockEntity getMaster() {
        return master;
    }
    
    public BlockPos getMasterPosition() {
        return getMaster().getBlockPos();
    }
    
    public BlockState getMasterBlockState() {
        return getMaster().getLevel().getBlockState(getMasterPosition());
    }
    
    public Direction getMasterDirection() {
        return getMasterBlockState().getValue(BlockStateProperties.FACING);
    }
    
    public boolean isBlockCorrect(BlockPos pos) {
        if (master.getLevel() == null) return false;
        for (Map<BlockPos, BlockState> map : getStructure()) {
            if (map.containsKey(pos)) {
                return master.getLevel().getBlockState(pos).equals(map.get(pos));
            }
        }
        return false;
    }
    
    public void createMissingParticles() {
        ParticleOptions smokeParticle = ParticleTypes.SMOKE;
        if (master.getLevel() == null) {
            Metallurgica.LOGGER.error("Level is null, cannot create particles");
            return;
        }
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                if (!isBlockCorrect(pos)) {
                    createCustomCubeParticles(pos, master.getLevel(), smokeParticle);
                }
            }
        }
    }
    
    public boolean isStructureCorrect() {
        for (Map<BlockPos, BlockState> map : getStructure()) {
            for (BlockPos pos : map.keySet()) {
                if (!isBlockCorrect(pos)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public BlockPos getRightSidePos(BlockPos pos) {
        return pos.relative(getMasterDirection().getClockWise());
    }
    public BlockPos getRightSidePos() {
        return getMasterPosition().relative(getMasterDirection().getClockWise());
    }
    public BlockPos getRightSidePos(int distance) {
        return getMasterPosition().relative(getMasterDirection().getClockWise(), distance);
    }
    public BlockPos getRightSidePos(BlockPos pos, int distance) {
        return pos.relative(getMasterDirection().getClockWise(), distance);
    }
    public BlockPos getRightSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getClockWise());
    }
    public BlockPos getRightSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getClockWise(), distance);
    }
    public BlockPos getLeftSidePos(BlockPos pos) {
        return pos.relative(getMasterDirection().getCounterClockWise());
    }
    public BlockPos getLeftSidePos() {
        return getMasterPosition().relative(getMasterDirection().getCounterClockWise());
    }
    public BlockPos getLeftSidePos(int distance) {
        return getMasterPosition().relative(getMasterDirection().getCounterClockWise(), distance);
    }
    public BlockPos getLeftSidePos(BlockPos pos, int distance) {
        return pos.relative(getMasterDirection().getCounterClockWise(), distance);
    }
    public BlockPos getLeftSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getCounterClockWise());
    }
    public BlockPos getLeftSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getCounterClockWise(), distance);
    }
}
