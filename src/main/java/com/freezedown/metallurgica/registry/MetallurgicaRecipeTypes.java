package com.freezedown.metallurgica.registry;

import com.drmangotea.createindustry.CreateTFMG;
import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.machines.blast_furnace.HeavyBlastingRecipe;
import com.google.common.collect.ImmutableSet;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public enum MetallurgicaRecipeTypes implements IRecipeTypeInfo {
    heavyBlasting(HeavyBlastingRecipe::new),
    ;
    
    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    private final @Nullable RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;
    public static final Set<ResourceLocation> RECIPE_DENY_SET = ImmutableSet.of(new ResourceLocation("occultism", "spirit_trade"), new ResourceLocation("occultism", "ritual"));
    
    MetallurgicaRecipeTypes(Supplier serializerSupplier, Supplier typeSupplier, boolean registerType) {
        String name = Lang.asId(this.name());
        this.id = CreateTFMG.asResource(name);
        this.serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            this.typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            this.type = this.typeObject;
        } else {
            this.typeObject = null;
            this.type = typeSupplier;
        }
        
    }
    
    MetallurgicaRecipeTypes(Supplier serializerSupplier) {
        String name = Lang.asId(this.name());
        this.id = CreateTFMG.asResource(name);
        this.serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        this.typeObject = Registers.TYPE_REGISTER.register(name, () -> {
            return simpleType(this.id);
        });
        this.type = this.typeObject;
    }
    
    private MetallurgicaRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory processingFactory) {
        this(() -> {
            return new ProcessingRecipeSerializer(processingFactory);
        });
    }
    
    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        final String stringId = id.toString();
        return new RecipeType<T>() {
            public String toString() {
                return stringId;
            }
        };
    }
    
    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) this.serializerObject.get();
    }
    
    public <T extends RecipeType<?>> T getType() {
        return (T) this.type.get();
    }
    
    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager().getRecipeFor(this.getType(), inv, world);
    }
    
    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER;
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER;
        
        private Registers() {
        }
        
        static {
            SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Metallurgica.ID);
            TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Metallurgica.ID);
        }
    }
}
