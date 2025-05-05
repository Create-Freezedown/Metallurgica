package com.freezedown.metallurgica.events;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours.interaction.FluidEntityInteractionHandler;
import com.freezedown.metallurgica.content.fluids.types.uf_backport.gas.GasMovementHandler;
import com.freezedown.metallurgica.experimental.exposure_effects.ExposureEffect;
import com.freezedown.metallurgica.experimental.exposure_effects.ExposureMinerals;
import com.freezedown.metallurgica.experimental.exposure_effects.ExposureUtil;
import com.freezedown.metallurgica.foundation.command.MetallurgicaCommands;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicDataPack;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import com.freezedown.metallurgica.foundation.data.runtime.MetallurgicaPackSource;
import com.freezedown.metallurgica.foundation.data.runtime.composition.RuntimeCompositions;
import com.freezedown.metallurgica.foundation.data.runtime.recipe.MetallurgicaRecipes;
import com.freezedown.metallurgica.infastructure.temperature.TemperatureHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
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
                serverLevel -> {
                    serverLevel.getEntities().getAll().forEach(FluidEntityInteractionHandler::handleInteraction);
                    TemperatureHandler.tick(serverLevel);
                }
        );
    }
    
    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        for (ExposureMinerals mineral : ExposureMinerals.values()) {
            boolean hasExposure = ExposureUtil.searchForExposer(player, mineral);
            MobEffectInstance effect = null;
            if (hasExposure && ExposureUtil.isEnabled(mineral)) {
                int timer = ExposureUtil.getExposureTimer(player, mineral);
                ExposureUtil.incrementExposureTimer(player, mineral);
                if (timer >= ExposureUtil.getStageOneMin(mineral) && timer < ExposureUtil.getStageTwoMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 1);
                } else if (timer >= ExposureUtil.getStageTwoMin(mineral) && timer < ExposureUtil.getStageThreeMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 2);
                } else if (timer >= ExposureUtil.getStageThreeMin(mineral) && timer < ExposureUtil.getStageFourMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 3);
                } else if (timer >= ExposureUtil.getStageFourMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 4);
                }
                if (effect != null) {
                    player.addEffect(effect);
                }
            }
        }
        int checkedEffects = 0;
        for (MobEffectInstance effect : player.getActiveEffects()) {
            MobEffect mobEffect = effect.getEffect();
            if (!(mobEffect instanceof ExposureEffect)) {
                checkedEffects++;
                continue;
            }
            if (checkedEffects >= player.getActiveEffects().size()) {
                player.getPersistentData().putBoolean("metallurgica:exposureEffect_showBlur", false);
                player.getPersistentData().putBoolean("metallurgica:exposureEffect_fatigue", false);
            }
        }
        //if (player instanceof ServerPlayer serverPlayer)
        //    MetallurgicaPackets.sendToPlayer(serverPlayer, /*new BlurShaderPacket(player.getPersistentData().getBoolean("metallurgica:exposureEffect_showBlur"))*/true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MetallurgicaCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                // Clear old data
                MetallurgicaDynamicResourcePack.clearClient();

                event.addRepositorySource(new MetallurgicaPackSource("metallurgica:dynamic_assets",
                        event.getPackType(),
                        Pack.Position.BOTTOM,
                        MetallurgicaDynamicResourcePack::new));
            } else if (event.getPackType() == PackType.SERVER_DATA) {
                MetallurgicaDynamicDataPack.clearServer();

                long startTime = System.currentTimeMillis();
                MetallurgicaRecipes.recipeRemoval();
                MetallurgicaRecipes.recipeAddition(MetallurgicaDynamicDataPack::addRecipe);
                RuntimeCompositions.compositionAddition(MetallurgicaDynamicDataPack::addComposition);
                Metallurgica.LOGGER.info("Metallurgica Data loading took {}ms", System.currentTimeMillis() - startTime);
                event.addRepositorySource(new MetallurgicaPackSource("metallurgica:dynamic_data", event.getPackType(), Pack.Position.BOTTOM, MetallurgicaDynamicDataPack::new));
            }
        }
    }
}
