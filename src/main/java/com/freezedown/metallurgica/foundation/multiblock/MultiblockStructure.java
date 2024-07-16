package com.freezedown.metallurgica.foundation.multiblock;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
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
    private final ArrayList<Map<BlockPos, TagKey<Block>>> tagStructure;
    private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
    
    public MultiblockStructure(BlockEntity master, ArrayList<Map<BlockPos, BlockState>> structure, ArrayList<Map<BlockPos, TagKey<Block>>> tagStructure) {
        this.master = master;
        this.structure = structure;
        this.tagStructure = tagStructure;
    }
    
    public MultiblockStructure addFluidOutput(BlockPos pos, String identifier) {
        fluidOutputs.add(Map.of(pos, identifier));
        return this;
    }
    public MultiblockStructure addFluidOutputs(List<Map<BlockPos, String>> fluidOutputs) {
        this.fluidOutputs.addAll(fluidOutputs);
        return this;
    }
    
    public static CuboidBuilder cuboidBuilder(BlockEntity master) {
        return new CuboidBuilder(master);
    }
    
    public static class CuboidBuilder {
        private final BlockEntity master;
        private Direction direction = null;
        private boolean isDirectional = false;
        private final ArrayList<Map<BlockPos, BlockState>> structure = new ArrayList<>();
        private final ArrayList<Map<BlockPos, TagKey<Block>>> tagStructure = new ArrayList<>();
        private final ArrayList<Map<BlockPos, String>> fluidOutputs = new ArrayList<>();
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
        
        public CuboidBuilder directional(Direction direction) {
            this.direction = direction;
            this.isDirectional = true;
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
        
        public CuboidBuilder withFluidOutputAt(int x, int y, int z, String identifier) {
            BlockPos pos = translateToMaster(x, y, z);
            fluidOutputs.add(Map.of(pos, identifier));
            return withBlockAt(x, y, z, MetallurgicaBlocks.fluidOutput.get().defaultBlockState());
        }
        
        public CuboidBuilder withBlockAt(int x, int y, int z, BlockState block) {
            BlockPos pos = translateToMaster(x, y, z);
            structure.add(Map.of(pos, block));
            return this;
        }
        
        public CuboidBuilder withBlockAt(PositionUtil.PositionRange range, BlockState block) {
            for (Triple<Integer, Integer, Integer> pos : range.getPositions()) {
                structure.add(Map.of(translateToMaster(pos.getFirst(), pos.getSecond(), pos.getThird()), block));
            }
            return this;
        }
        
        public CuboidBuilder withTagAt(int x, int y, int z, TagKey<Block> blockTag) {
            BlockPos pos = translateToMaster(x, y, z);
            tagStructure.add(Map.of(pos, blockTag));
            return this;
        }
        
        public CuboidBuilder withTagAt(PositionUtil.PositionRange range, TagKey<Block> blockTag) {
            for (Triple<Integer, Integer, Integer> pos : range.getPositions()) {
                tagStructure.add(Map.of(translateToMaster(pos.getFirst(), pos.getSecond(), pos.getThird()), blockTag));
            }
            return this;
        }
        
        
        
        private BlockPos translateToMaster(int x, int y, int z) {
            BlockPos masterPos = getMasterPosition();
            if (isDirectional) {
                return masterPos.relative(direction, x).above(y).relative(direction.getClockWise(), z);
            }
            return masterPos.offset(x, y, z);
        }
        
        public MultiblockStructure build() {
            return new MultiblockStructure(getMaster(), structure, tagStructure).addFluidOutputs(fluidOutputs);
        }
    }
    
    public ArrayList<Map<BlockPos, BlockState>> getStructure() {
        return structure;
    }
    
    public ArrayList<Map<BlockPos, TagKey<Block>>> getTagStructure() {
        return tagStructure;
    }
    
    public BlockEntity getMaster() {
        return master;
    }
    
    public ArrayList<Map<BlockPos, String>> getFluidOutputs() {
        return fluidOutputs;
    }
    
    public BlockPos getFluidOutputPosition(String identifier) {
        for (Map<BlockPos, String> map : fluidOutputs) {
            for (BlockPos pos : map.keySet()) {
                if (map.get(pos).equals(identifier)) {
                    return pos;
                }
            }
        }
        return null;
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
            for (BlockPos pos1 : map.keySet()) {
                return master.getLevel().getBlockState(pos).equals(map.get(pos1));
            }
        }
        
        return false;
    }
    
    public boolean isTagCorrect(BlockPos pos) {
        if (master.getLevel() == null) return false;
        for (Map<BlockPos, TagKey<Block>> tagMap : getTagStructure()) {
            for (BlockPos pos1 : tagMap.keySet()) {
                return master.getLevel().getBlockState(pos).is(tagMap.get(pos1));
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
                    Metallurgica.LOGGER.debug("Invalid block at {} in multiblock structure. must be {}", pos, map.get(pos));
                }
            }
        }
        for (Map<BlockPos, TagKey<Block>> tagMap : getTagStructure()) {
            for (BlockPos pos : tagMap.keySet()) {
                if (!isTagCorrect(pos)) {
                    createCustomCubeParticles(pos, master.getLevel(), smokeParticle);
                    Metallurgica.LOGGER.debug("Invalid tag at {} in multiblock structure. must be {}", pos, tagMap.get(pos));
                }
            }
        }
    }
    
    public boolean isStructureCorrect() {
        int validBlocks = getStructure().size() + getTagStructure().size() - 1;
        for (Map<BlockPos, BlockState> map : getStructure()) {
                for (BlockPos pos : map.keySet()) {
                    if (!isBlockCorrect(pos)) {
                        validBlocks--;
                    }
                }
            }
        for (Map<BlockPos, TagKey<Block>> tagMap : getTagStructure()) {
            for (BlockPos pos : tagMap.keySet()) {
                if (!isTagCorrect(pos)) {
                    validBlocks--;
                }
            }
        }
        return validBlocks >= getStructure().size() + getTagStructure().size() - 1;
    }
    
    public void setFluidOutputCapacity(FluidOutputBlockEntity fluidOutput, int value) {
        if (fluidOutput == null) return;
        if (fluidOutput.tankInventory.getCapacity() == value) return;
        fluidOutput.tankInventory.setCapacity(value);
    }
}
