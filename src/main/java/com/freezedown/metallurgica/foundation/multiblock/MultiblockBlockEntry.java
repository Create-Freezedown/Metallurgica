package com.freezedown.metallurgica.foundation.multiblock;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockBlockEntry {
    private final TagKey<Block> blockTag;
    private final Block block;
    private final BlockState blockState;
    
    public MultiblockBlockEntry(TagKey<Block> blockTag, Block block, BlockState blockState) {
        this.blockTag = blockTag;
        this.block = block;
        this.blockState = blockState;
    }
    
    public MultiblockBlockEntry withTag(TagKey<Block> blockTag) {
        return new MultiblockBlockEntry(blockTag, block, blockState);
    }
    
    public MultiblockBlockEntry withBlock(Block block) {
        return new MultiblockBlockEntry(blockTag, block, blockState);
    }
    
    public MultiblockBlockEntry withBlockState(BlockState blockState) {
        return new MultiblockBlockEntry(blockTag, block, blockState);
    }
    
    public TagKey<Block> getBlockTag() {
        return blockTag;
    }
    public BlockState getBlockState() {
        return blockState;
    }
    public Block getBlock() {
        return block;
    }
}
