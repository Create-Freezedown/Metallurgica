package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.foundation.client.renderer.MaterialBlockRenderer;
import com.freezedown.metallurgica.foundation.client.renderer.PillarMaterialBlockRenderer;
import com.freezedown.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = ModelManager.class)
public abstract class ModelManagerMixin {

    @Inject(method = "reload", at = @At(value = "HEAD"))
    private void metallurgica$loadDynamicModels(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (!ModLoader.isLoadingStateValid()) return;

        long startTime = System.currentTimeMillis();
        MaterialBlockRenderer.reinitModels();
        PillarMaterialBlockRenderer.reinitModels();
        MetallurgicaModels.registerMaterialAssets();
        Metallurgica.LOGGER.info("Metallurgica Model loading took {}ms", System.currentTimeMillis() - startTime);
    }
}
