package com.freezedown.metallurgica.registry;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.UP;

public class MetallurgicaShapes {
    // Independent Shapers
    //public static final VoxelShaper
    
    //;
    // Static Block Shapes
    public static final VoxelShape
            spoutputCeiling = shape( 3, 12,  3, 13, 16, 13).build(),
            spoutputWallSouth = shape(3,  4,  0, 13, 14,  4).build(),
            spoutputWallNorth = shape(3,  4, 12, 13, 14, 16).build(),
            spoutputWallEast  = shape(0,  4,  3,  4, 14, 13).build(),
            spoutputWallWest  = shape(12, 4,  3, 16, 14, 13).build(),
            ceramicPot = shape(2, 0, 2, 14, 15, 14).erase(4, 2, 4, 12, 15, 12).build()
    ;
    
    private static MetallurgicaShapes.Builder shape(VoxelShape shape) {
        return new MetallurgicaShapes.Builder(shape);
    }
    
    private static MetallurgicaShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }
    
    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
    
    public static class Builder {
        
        private VoxelShape shape;
        
        public Builder(VoxelShape shape) {
            this.shape = shape;
        }
        
        public MetallurgicaShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }
        
        public MetallurgicaShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }
        
        public MetallurgicaShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }
        
        public VoxelShape build() {
            return shape;
        }
        
        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }
        
        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }
        
        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }
        
        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }
        
        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }
        
        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }
        
        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }
        
    }
}
