package com.freezedown.metallurgica.content.machines.reaction_basin;

import com.freezedown.metallurgica.foundation.util.MetalLang;
import com.freezedown.metallurgica.registry.MetallurgicaShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.ForgeSoundType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReactionBasinBlock extends Block implements IWrenchable {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);
    
    public ReactionBasinBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(SHAPE, Shape.PLAIN));
    }
    
    public static boolean isRB(BlockState state) {
        return state.getBlock() instanceof ReactionBasinBlock;
    }
    
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getCollision(pState);
    }
    
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (!pState.getValue(BOTTOM) && pState.getValue(SHAPE) == Shape.CENTER)
            return Shapes.empty();
        return getCollision(pState);
    }
    
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return getCollision(pState);
    }
    
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }
    
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM, SHAPE);
    }
    
    // Reaction Basins are less noisy when placed in batch
    public static final SoundType SILENCED_METAL =
            new ForgeSoundType(0.1F, 1.5F, () -> SoundEvents.METAL_BREAK, () -> SoundEvents.METAL_STEP,
                    () -> SoundEvents.METAL_PLACE, () -> SoundEvents.METAL_HIT, () -> SoundEvents.METAL_FALL);
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.NONE)
            return state;
        boolean x = mirror == Mirror.FRONT_BACK;
        return switch (state.getValue(SHAPE)) {
            case N -> state.setValue(SHAPE, x ? Shape.S : Shape.N);
            case S -> state.setValue(SHAPE, x ? Shape.N : Shape.S);
            case W -> state.setValue(SHAPE, x ? Shape.E : Shape.W);
            case E -> state.setValue(SHAPE, x ? Shape.W : Shape.E);
            case NE -> state.setValue(SHAPE, x ? Shape.NW : Shape.SE);
            case NW -> state.setValue(SHAPE, x ? Shape.NE : Shape.SW);
            case SE -> state.setValue(SHAPE, x ? Shape.SW : Shape.NE);
            case SW -> state.setValue(SHAPE, x ? Shape.SE : Shape.NW);
            default -> state;
        };
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        for (int i = 0; i < rotation.ordinal(); i++)
            state = rotateOnce(state);
        return state;
    }
    
    private BlockState rotateOnce(BlockState state) {
        return switch (state.getValue(SHAPE)) {
            case N -> state.setValue(SHAPE, Shape.E);
            case S -> state.setValue(SHAPE, Shape.W);
            case W -> state.setValue(SHAPE, Shape.N);
            case E -> state.setValue(SHAPE, Shape.S);
            case NE -> state.setValue(SHAPE, Shape.SE);
            case NW -> state.setValue(SHAPE, Shape.NE);
            case SE -> state.setValue(SHAPE, Shape.SW);
            case SW -> state.setValue(SHAPE, Shape.NW);
            default -> state;
        };
    }
    
    public static enum Shape implements StringRepresentable {
        PLAIN,
        CENTER,
        N,
        S,
        W,
        E,
        NW,
        SW,
        NE,
        SE;
        
        private Shape() {
        }
        
        public String getSerializedName() {
            return MetalLang.asId(this.name());
        }
    }
    
    private VoxelShape getCollision(BlockState state) {
        Boolean top = state.getValue(ReactionBasinBlock.TOP);
        Boolean bottom = state.getValue(ReactionBasinBlock.BOTTOM);
        ReactionBasinBlock.Shape shape = state.getValue(ReactionBasinBlock.SHAPE);
        return switch (shape) {
            case CENTER -> top || !bottom ? MetallurgicaShapes.rbEmpty : MetallurgicaShapes.rbCenter;
            case PLAIN -> top && bottom ? MetallurgicaShapes.rbSingle : !bottom ? MetallurgicaShapes.rbSingleMiddle : MetallurgicaShapes.rbSingle;
            case NW -> top && bottom ? MetallurgicaShapes.rbSingleNW : !bottom ? !top ? MetallurgicaShapes.rbMiddleNW : MetallurgicaShapes.rbTopNW : MetallurgicaShapes.rbSingleNW;
            case SW -> top && bottom ? MetallurgicaShapes.rbSingleSW : !bottom ? !top ? MetallurgicaShapes.rbMiddleSW : MetallurgicaShapes.rbTopSW : MetallurgicaShapes.rbSingleSW;
            case NE -> top && bottom ? MetallurgicaShapes.rbSingleNE : !bottom ? !top ? MetallurgicaShapes.rbMiddleNE : MetallurgicaShapes.rbTopNE : MetallurgicaShapes.rbSingleNE;
            case SE -> top && bottom ? MetallurgicaShapes.rbSingleSE : !bottom ? !top ? MetallurgicaShapes.rbMiddleSE : MetallurgicaShapes.rbTopSE : MetallurgicaShapes.rbSingleSE;
            case N -> top && bottom ? MetallurgicaShapes.rbSingleN : !bottom ? !top ? MetallurgicaShapes.rbMiddleN : MetallurgicaShapes.rbTopN : MetallurgicaShapes.rbSingleN;
            case S -> top && bottom ? MetallurgicaShapes.rbSingleS : !bottom ? !top ? MetallurgicaShapes.rbMiddleS : MetallurgicaShapes.rbTopS : MetallurgicaShapes.rbSingleS;
            case W -> top && bottom ? MetallurgicaShapes.rbSingleW : !bottom ? !top ? MetallurgicaShapes.rbMiddleW : MetallurgicaShapes.rbTopW : MetallurgicaShapes.rbSingleW;
            case E -> top && bottom ? MetallurgicaShapes.rbSingleE : !bottom ? !top ? MetallurgicaShapes.rbMiddleE : MetallurgicaShapes.rbTopE : MetallurgicaShapes.rbSingleE;
        };
    }
}
