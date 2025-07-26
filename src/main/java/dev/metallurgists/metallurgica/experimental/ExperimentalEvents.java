package dev.metallurgists.metallurgica.experimental;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.experimental.exposure_effects.LeadPoisoningEffect;
import com.mojang.blaze3d.shaders.AbstractUniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class ExperimentalEvents {
    @Nullable
    private PostChain postEffect;
    
    public void shutdownEffect() {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        
        this.postEffect = null;
    }
    
    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        Level level = event.player.level();
        
        if (level.isClientSide() && level.getGameTime() % 10 == 0) {
            
            LocalPlayer player = Minecraft.getInstance().player;
            
            if (player == null) {
                Metallurgica.LOGGER.error("ExperimentalEvents$onUpdate Player is null");
                return;
            }
            
            for (MobEffectInstance mobEffectInstance : player.getActiveEffects()) {
                
                if (mobEffectInstance.getEffect() instanceof LeadPoisoningEffect && mobEffectInstance.getAmplifier() >= 2) {
                    setShader(Metallurgica.asResource("blur"));
                    return;
                }
            }
            
            clearShader();
        }
    }
    
    
    @OnlyIn(Dist.CLIENT)
    private void clearShader() {
        
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        
        if (renderer.currentEffect() != null) {
            renderer.shutdownEffect();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void setShader(ResourceLocation shader) {
        GameRenderer renderer = Minecraft.getInstance().gameRenderer;
        renderer.loadEffect(new ResourceLocation(shader.getNamespace(), "shaders/post/" + shader.getPath() + ".json"));
    }
    
    @OnlyIn(Dist.CLIENT)
    private ShaderInstance getShader(String shaderName) {
            GameRenderer renderer = Minecraft.getInstance().gameRenderer;
            return renderer.getShader(shaderName);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void updateShaderUniforms(ShaderInstance shader, String uniformName, float value) {
        if (shader == null) {
            Metallurgica.LOGGER.error("ExperimentalEvents$updateShaderUniforms Shader is null");
            return;
        }
        AbstractUniform uniform = shader.safeGetUniform(uniformName);
        uniform.set(value);
    }
}
