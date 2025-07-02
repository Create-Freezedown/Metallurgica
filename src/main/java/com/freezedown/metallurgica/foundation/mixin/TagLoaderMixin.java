package com.freezedown.metallurgica.foundation.mixin;

import com.freezedown.metallurgica.foundation.IMetallurgicaTagLoader;
import com.freezedown.metallurgica.foundation.MixinHelpers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = TagLoader.class, priority = 500)
public class TagLoaderMixin<T> implements IMetallurgicaTagLoader<T> {

    @Nullable
    @Unique
    private Registry<T> metallurgica$storedRegistry;

    @Inject(method = "load", at = @At(value = "RETURN"))
    public void metallurgica$load(ResourceManager resourceManager,
                           CallbackInfoReturnable<Map<ResourceLocation, List<TagLoader.EntryWithSource>>> cir) {
        var tagMap = cir.getReturnValue();
        if (metallurgica$getRegistry() == null) return;
        MixinHelpers.generateDynamicTags(tagMap, metallurgica$getRegistry());
    }

    @Override
    public void metallurgica$setRegistry(Registry<T> registry) {
        this.metallurgica$storedRegistry = registry;
    }

    @Override
    public @Nullable Registry<T> metallurgica$getRegistry() {
        return metallurgica$storedRegistry;
    }
}
