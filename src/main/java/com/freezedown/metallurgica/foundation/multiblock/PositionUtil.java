package com.freezedown.metallurgica.foundation.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PositionUtil {
    
    
    public static BlockPos getLeftSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getCounterClockWise());
    }
    public static BlockPos getLeftSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getCounterClockWise(), distance);
    }
    public static BlockPos getRightSidePos(BlockPos pos, Direction direction) {
        return pos.relative(direction.getClockWise());
    }
    public static BlockPos getRightSidePos(BlockPos pos, int distance, Direction direction) {
        return pos.relative(direction.getClockWise(), distance);
    }
}
