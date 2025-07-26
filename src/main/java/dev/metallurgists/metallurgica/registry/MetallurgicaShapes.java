package dev.metallurgists.metallurgica.registry;

import net.createmod.catnip.math.VoxelShaper;
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
            spoutputCeiling = shape(3, 12,  3, 13, 16, 13).build(),
            spoutputWallSouth = shape(3,  4,  0, 13, 14,  4).build(),
            spoutputWallNorth = shape(3,  4, 12, 13, 14, 16).build(),
            spoutputWallEast  = shape(0,  4,  3,  4, 14, 13).build(),
            spoutputWallWest  = shape(12, 4,  3, 16, 14, 13).build(),
            ceramicPot = shape(2, 0, 2, 14, 15, 14).erase(4, 2, 4, 12, 15, 12).build(),
            crucible = shape(2, 0, 2, 14, 12, 14).erase(4, 2, 4, 12, 15, 12).build(),
            
    
            colonThree = null
                    ;
    
    public static final VoxelShaper
            ingotCastingMold = shape(3, 0, 1, 13, 6, 15).erase(5, 2, 3, 11, 6, 13).forHorizontal(Direction.NORTH),
    
            bleh = null;
            ;
    // Reaction Basin Shapes
    public static final VoxelShape
            rbSingle = shape(1, 0, 1, 15, 2, 15).add(0, 2, 0, 16, 16, 16).erase(2, 3, 2, 14, 16, 14).build(),
            rbSingleMiddle = shape(0, 0, 0, 16, 16, 16).erase(2, 0, 2, 14, 16, 14).build(),
            rbSingleTop = rbSingleMiddle,
            rbSingleNW = shape(2, 0, 2, 16, 2, 16).add(0, 2, 0, 16, 16, 16).erase(2, 3, 2, 16, 16, 16).build(),
            rbSingleNE = shape(0, 0, 2, 14, 2, 16).add(0, 2, 0, 16, 16, 16).erase(0, 3, 2, 14, 16, 16).build(),
            rbSingleSW = shape(2, 0, 0, 16, 2, 14).add(0, 2, 0, 16, 16, 16).erase(2, 3, 0, 16, 16, 14).build(),
            rbSingleSE = shape(0, 0, 0, 14, 2, 14).add(0, 2, 0, 16, 16, 16).erase(0, 3, 0, 14, 16, 14).build(),
            rbMiddleNW = shape(0, 0, 0, 16, 16, 16).erase(2, 0, 2, 16, 16, 16).build(),
            rbMiddleNE = shape(0, 0, 0, 16, 16, 16).erase(0, 0, 2, 14, 16, 16).build(),
            rbMiddleSW = shape(0, 0, 0, 16, 16, 16).erase(2, 0, 0, 16, 16, 14).build(),
            rbMiddleSE = shape(0, 0, 0, 16, 16, 16).erase(0, 0, 0, 14, 16, 14).build(),
            rbTopNW = shape(0, 0, 0, 16, 16, 16).erase(2, 0, 2, 16, 16, 16).build(),
            rbTopNE = shape(0, 0, 0, 16, 16, 16).erase(0, 0, 2, 14, 16, 16).build(),
            rbTopSW = shape(0, 0, 0, 16, 16, 16).erase(2, 0, 0, 16, 16, 14).build(),
            rbTopSE = shape(0, 0, 0, 16, 16, 16).erase(0, 0, 0, 14, 16, 14).build(),
            rbSingleN = shape(0, 0, 2, 16, 2, 16).add(0, 2, 0, 16, 16, 16).erase(0, 3, 2, 16, 16, 16).build(),
            rbSingleS = shape(0, 0, 0, 16, 2, 14).add(0, 2, 0, 16, 16, 16).erase(0, 3, 0, 16, 16, 14).build(),
            rbSingleW = shape(2, 0, 0, 16, 2, 16).add(0, 2, 0, 16, 16, 16).erase(2, 3, 0, 16, 16, 16).build(),
            rbSingleE = shape(0, 0, 0, 14, 2, 16).add(0, 2, 0, 16, 16, 16).erase(0, 3, 0, 14, 16, 16).build(),
            rbMiddleN = shape(0, 0, 0, 16, 16, 2).build(),
            rbMiddleS = shape(0, 0, 14, 16, 16, 16).build(),
            rbMiddleW = shape(0, 0, 0, 2, 16, 16).build(),
            rbMiddleE = shape(14, 0, 0, 16, 16, 16).build(),
            rbTopN = shape(0, 0, 0, 16, 16, 2).build(),
            rbTopS = shape(0, 0, 14, 16, 16, 16).build(),
            rbTopW = shape(0, 0, 0, 2, 16, 16).build(),
            rbTopE = shape(14, 0, 0, 16, 16, 16).build(),
            rbCenter = shape(0, 0, 0, 16, 3, 16).build(),
            rbEmpty = shape(0, 0, 0, 16, 16, 16).build()
            ;
    
    public static VoxelShaper ingotMold(Direction direction) {
        return shape(3, 0, 1, 13, 6, 15).erase(5, 2, 3, 11, 6, 13).forHorizontal(direction);
    }

    public static VoxelShaper kilnChamber() {
        return shape(0, 0, 0, 16, 16, 16).erase(4, 0, 4, 16, 16, 16).forHorizontal(Direction.NORTH);
    }

    public static VoxelShaper kilnBase() {
        return shape(0, 0, 0, 16, 16, 16).erase(4, 3, 4, 16, 16, 16).forHorizontal(Direction.NORTH);
    }

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
