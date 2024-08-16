package com.freezedown.metallurgica.registry.misc;

import com.freezedown.metallurgica.Metallurgica;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public class MetallurgicaShaders {
    public static ShaderInstance blurShader;
    
    public static void renderShaders(RegisterShadersEvent event) {
        ResourceProvider resourceProvider = event.getResourceManager();
        try {
            event.registerShader(new ShaderInstance(resourceProvider, new ResourceLocation(Metallurgica.ID, "blur"), DefaultVertexFormat.POSITION_COLOR), instance -> blurShader = instance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
