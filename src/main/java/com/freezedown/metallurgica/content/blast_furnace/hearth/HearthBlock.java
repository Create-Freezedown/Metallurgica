package com.freezedown.metallurgica.content.blast_furnace.hearth;

import com.freezedown.metallurgica.registry.misc.MetallurgicaBlockPatterns;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class HearthBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty validStructure = BooleanProperty.create("valid_structure");
    public HearthBlock(Properties pProperties) {
        super(pProperties);
    }
    
    private BlockPos hearthStructurePos(Direction direction, BlockPos pos) {
        return switch (direction) {
            case NORTH -> pos.south().south().below();
            case SOUTH -> pos.north().north().below();
            case EAST -> pos.west().west().below();
            case WEST -> pos.east().east().below();
            default -> pos;
        };
    }
    public void updateState(BlockState state, Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch patternHelper = MetallurgicaBlockPatterns.industrialBlastFurnace.find(level, hearthStructurePos(state.getValue(FACING), pos));
        if (patternHelper == null) {
            if (state.getValue(validStructure)) {
                level.setBlockAndUpdate(pos, state.setValue(validStructure, false));
            }
        } else if (!state.getValue(validStructure)) {
            level.setBlockAndUpdate(pos, state.setValue(validStructure, true));
        }
    }
}
