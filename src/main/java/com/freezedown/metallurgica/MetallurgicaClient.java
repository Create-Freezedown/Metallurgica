package com.freezedown.metallurgica;

import com.freezedown.metallurgica.foundation.material.block.renderer.MaterialCogWheelRenderer;
import com.freezedown.metallurgica.foundation.ponder.MetallurgicaPonderPlugin;
import com.freezedown.metallurgica.registry.MetallurgicaPartialModels;
import com.freezedown.metallurgica.registry.material.init.MetMaterialBlockEntities;
import com.freezedown.metallurgica.registry.material.init.MetMaterialPartialModels;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class MetallurgicaClient {
    
    public MetallurgicaClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        MetallurgicaPartialModels.init();
        MetMaterialPartialModels.clientInit();
        
        modEventBus.register(this);
    }
    
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(MetallurgicaClient::clientInit);
    }
    
    @SubscribeEvent
    public static void clientInit(final FMLClientSetupEvent event) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PonderIndex.addPlugin(new MetallurgicaPonderPlugin());

        BlockEntityRenderers.register(MetMaterialBlockEntities.materialCogwheel.get(), MaterialCogWheelRenderer::new);
    }


}
