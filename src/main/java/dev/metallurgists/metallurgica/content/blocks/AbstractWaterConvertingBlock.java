package dev.metallurgists.metallurgica.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.*;

import java.util.List;

public abstract class AbstractWaterConvertingBlock extends Block {

    public AbstractWaterConvertingBlock(Properties properties) {
        super(properties);
    }

    abstract FlowingFluid convertTo();

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<Direction> facesWithWater = Direction.stream().filter(dir -> isWater(level.getFluidState(pos.relative(dir)))).toList();
        boolean shouldTick = !facesWithWater.isEmpty();
        if (!shouldTick) return;
        for (Direction face : facesWithWater) {
            BlockPos facePos = pos.relative(face);
            if (facePos.equals(pos)) continue;
            BlockState neighborState = level.getBlockState(facePos);
            convertWater(level, facePos);
            if (neighborState.getFluidState().getType().isSame(Fluids.WATER)) {
                convertWaterBesideNeighborWater(level, pos, face);
            }
        }
    }

    private void convertWaterBesideNeighborWater(ServerLevel level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        List<Direction> neighborFacesWithWater = Direction.stream().filter(dir -> isWater(level.getFluidState(neighborPos.relative(dir)))).toList();
        if (!neighborFacesWithWater.isEmpty()) {
            for (Direction face : neighborFacesWithWater) {
                BlockPos facePos = neighborPos.relative(face);
                if (facePos.equals(pos)) continue;
                convertWater(level, facePos);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void convertWater(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        RandomSource random = level.getRandom();
        if (random.nextInt(12) <= 6) return;
        FluidState fluidState = state.getFluidState();
        boolean isFlowing = !fluidState.isSource();
        FluidState newFluidState = isFlowing ? convertTo().getFlowing(fluidState.getValue(BlockStateProperties.LEVEL_FLOWING), fluidState.getValue(BlockStateProperties.FALLING)) : convertTo().getSource(fluidState.getValue(BlockStateProperties.FALLING));
        if (state.getFluidState().is(Fluids.WATER) && !isFlowing) {
            level.setBlockAndUpdate(pos, newFluidState.createLegacyBlock());
        }
    }

    private boolean isWater(FluidState state) {
        return state.getType().isSame(Fluids.WATER) || state.getType().isSame(convertTo());
    }
}
