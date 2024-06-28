package com.freezedown.metallurgica.content.fluids.types.uf_backport.gas;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GasMovementHandler {
    public static final Map<Pair<LevelAccessor, FlowingGas>, GasMovementHandler> handlers = new HashMap<>();
    
    protected final LevelAccessor level;
    protected final FlowingGas source;
    
    private SimpleWeightedGraph<BlockPos, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    
    private GasMovementHandler(LevelAccessor level, FlowingGas source) {
        this.level = level;
        this.source = source;
    }
    
    private GasMovementHandler(Pair<LevelAccessor, FlowingGas> pair) {
        this(pair.getFirst(), pair.getSecond());
    }
    
    protected List<GasMovement> operations = new ArrayList<>();
    
    protected long lastTick = 0;
    
    public void tick(long tick) {
        if (tick >= lastTick) {
            lastTick = tick;
        }
        
        while (!operations.isEmpty()) {
            GasMovement operation = operations.get(0);
            operations.remove(0);
            
            if (operation.movements.stream().anyMatch(movement ->
                    getDensity(movement.getFirst()) == -1 ||
                            getDensity(movement.getFirst()) + movement.getSecond() > FlowingGas.MAX_DENSITY ||
                            getDensity(movement.getFirst()) + movement.getSecond() < 0
            )) {
                continue;
            }
            
            for (Pair<BlockPos, Integer> movement : operation.movements) {
                BlockPos pos = movement.getFirst();
                int resultDensity = getDensity(pos) + movement.getSecond();
                
                if (pos.getY() < level.getMinBuildHeight() || pos.getY() > level.getMaxBuildHeight()) continue;
                
                if (resultDensity == 0)
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                else if (resultDensity == FlowingGas.MAX_DENSITY)
                    level.setBlock(pos, source.getSource().defaultFluidState().createLegacyBlock(), 11);
                else
                    level.setBlock(pos, source.getFlowing(resultDensity, false).createLegacyBlock(), 11);
            }
        }
    }
    
    protected int getDensity(BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getFluidState().is(source.getSource())) return state.getValue(FlowingGas.DENSITY);
        if (state.getFluidState().is(source.getFlowing())) return state.getValue(FlowingGas.DENSITY);
        return state.canBeReplaced(source) ? 0 : -1;
    }
    
    public void move(BlockPos pos, int density, Direction direction) {
        operations.add(GasMovement.create()
                .decrease(pos, density)
                .increase(pos.relative(direction), density));
    }
    
    public SimpleWeightedGraph<BlockPos, DefaultWeightedEdge> getGraph() {
        return graph;
    }
    
    public void setGraph(SimpleWeightedGraph<BlockPos, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }
    
    public static class GasMovement {
        protected ArrayList<Pair<BlockPos, Integer>> movements = new ArrayList<>();
        
        public static GasMovement create() {
            return new GasMovement();
        }
        
        public GasMovement() {}
        
        public GasMovement increase(BlockPos pos, int density) {
            movements.add(new Pair<>(pos, density));
            return this;
        }
        
        public GasMovement decrease(BlockPos pos, int density) {
            movements.add(new Pair<>(pos, -density));
            return this;
        }
    }
    
    public static GasMovementHandler getOrCreate(LevelAccessor level, FlowingGas gas) {
        return handlers.computeIfAbsent(Pair.of(level, (FlowingGas) gas.getSource()), GasMovementHandler::new);
    }
}
