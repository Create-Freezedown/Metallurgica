package com.freezedown.metallurgica.events;

import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction.FluidEntityInteractionHandler;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasMovementHandler;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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
