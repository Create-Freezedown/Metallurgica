package com.freezedown.metallurgica.events;

import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction.FluidEntityInteractionHandler;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasMovementHandler;
import net.minecraft.core.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

public class CommonEvents {
    
    @SubscribeEvent
    public void serverTickEvent(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
        GasMovementHandler.handlers.forEach(
                (pair, handler) -> {
                    SimpleWeightedGraph<BlockPos, DefaultWeightedEdge> graph = handler.getGraph();
                    List<DefaultWeightedEdge> removingEdges = new ArrayList<>();
                    graph.edgeSet().forEach(edge -> {
                        graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) - 1d/(20 * 60));
                        double weight = graph.getEdgeWeight(edge);
                        if (weight <= 0)
                            removingEdges.add(edge);
                    });
                    
                    removingEdges.forEach(graph::removeEdge);
                    
                    List<BlockPos> removingVertexes = new ArrayList<>();
                    
                    graph.vertexSet().forEach(vertex -> {
                        if (graph.edgesOf(vertex).isEmpty())
                            removingVertexes.add(vertex);
                    });
                    
                    removingVertexes.forEach(graph::removeVertex);
                }
        );
        
        event.getServer().getAllLevels().forEach(
                serverLevel -> serverLevel.getEntities().getAll().forEach(
                        FluidEntityInteractionHandler::handleInteraction
                )
        );
    }
}
