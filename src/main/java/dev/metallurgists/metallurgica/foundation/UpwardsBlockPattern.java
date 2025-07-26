package dev.metallurgists.metallurgica.foundation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class UpwardsBlockPattern extends BlockPattern {
    
    public UpwardsBlockPattern(Predicate<BlockInWorld>[][][] pattern) {
        super(pattern);
    }
    
    public static UpwardsBlockPattern of(BlockPatternBuilder blockPatternBuilder) {
        return new UpwardsBlockPattern(blockPatternBuilder.createPattern());
    }
    
    @Nullable
    @Override
    public BlockPatternMatch find(@Nonnull LevelReader level, @Nonnull BlockPos pos) {
        int i = Math.max(this.getWidth(), this.getDepth());
        
        for(BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-i + 1, 0, -i + 1), pos.offset(i - 1, this.getHeight(), i - 1))) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPatternMatch patternMatch = this.matches(level, blockPos, direction, Direction.UP);
                
                if (patternMatch != null) {
                    return patternMatch;
                }
            }
        }
        
        return null;
    }
}
