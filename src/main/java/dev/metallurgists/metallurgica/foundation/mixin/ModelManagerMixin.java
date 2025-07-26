package dev.metallurgists.metallurgica.foundation.mixin;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.client.renderer.MaterialBlockRenderer;
import dev.metallurgists.metallurgica.foundation.client.renderer.MaterialCogWheelBlockRenderer;
import dev.metallurgists.metallurgica.foundation.client.renderer.PillarMaterialBlockRenderer;
import dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels;
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
        MaterialCogWheelBlockRenderer.reinitModels();
        MetallurgicaModels.registerMaterialAssets();
        Metallurgica.LOGGER.info("Metallurgica Model loading took {}ms", System.currentTimeMillis() - startTime);
    }
}
