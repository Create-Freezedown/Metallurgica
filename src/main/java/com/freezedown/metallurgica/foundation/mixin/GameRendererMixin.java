package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.registry.MetallurgicaEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    
    @Shadow public abstract void loadEffect(ResourceLocation pResourceLocation);
    
    @Shadow public abstract void onResourceManagerReload(ResourceManager pResourceManager);
    
    @Shadow @Final private ResourceManager resourceManager;
    
    //@Inject(method = "checkEntityPostEffect", at = @At("TAIL"))
    //private void checkEntityPostEffect(Entity pEntity, CallbackInfo ci) {
    //    boolean flag = false;
    //    if (this.minecraft.level != null) {
    //        this.minecraft.getProfiler().popPush("gui");
    //        if (this.minecraft.player != null) {
    //            Collection<MobEffectInstance> effects = this.minecraft.player.getActiveEffects();
    //            for (MobEffectInstance effect : effects) {
    //                if (effect.getEffect() == MetallurgicaEffects.LEAD_POISONING.get()) {
    //                    if (effect.getAmplifier() >= 2) {
    //                        flag = true;
    //                        Metallurgica.LOGGER.info("Lead poisoning detected!");
    //                        onResourceManagerReload(resourceManager);
    //                        break;
    //                    }
    //                }
    //            }
    //        }
    //    }
    //    if (pEntity instanceof Player && flag) {
    //        this.loadEffect(new ResourceLocation("shaders/post/blur.json"));
    //    }
    //}
    
    //@Inject(method = "cycleEffect", at = @At("TAIL"))
    //private void cycleEffect(CallbackInfo ci) {
    //    boolean flag = false;
    //    if (this.minecraft.level != null) {
    //        this.minecraft.getProfiler().popPush("gui");
    //        if (this.minecraft.player != null) {
    //            Collection<MobEffectInstance> effects = this.minecraft.player.getActiveEffects();
    //            for (MobEffectInstance effect : effects) {
    //                if (effect.getEffect() == MetallurgicaEffects.LEAD_POISONING.get()) {
    //                    if (effect.getAmplifier() >= 2) {
    //                        flag = true;
    //                        Metallurgica.LOGGER.info("Lead poisoning detected!");
    //                        break;
    //                    }
    //                }
    //            }
    //        }
    //    }
    //    if (this.minecraft.getCameraEntity() instanceof Player && flag) {
    //        this.loadEffect(new ResourceLocation("shaders/post/blur.json"));
    //    }
    //}
}
