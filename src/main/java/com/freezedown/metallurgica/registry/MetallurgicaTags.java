package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.MOD;
import static com.freezedown.metallurgica.registry.MetallurgicaTags.NameSpace.TFMG;

public class MetallurgicaTags {
    public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry,
                                            ResourceLocation id) {
        return registry.tags()
                .createOptionalTagKey(id, Collections.emptySet());
    }
    public static <T> TagKey<T> modTag(IForgeRegistry<T> registry, String namespace, String path) {
        return optionalTag(registry, new ResourceLocation(namespace, path));
    }
    public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("forge", path));
    }
    
    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(ForgeRegistries.BLOCKS, path);
    }
    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ForgeRegistries.ITEMS, path);
    }
    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(ForgeRegistries.FLUIDS, path);
    }
    
    public static TagKey<Block> modBlockTag(String namespace, String path) {
        return modTag(ForgeRegistries.BLOCKS, namespace, path);
    }
    public static TagKey<Item> modItemTag(String namespace, String path) {
        return modTag(ForgeRegistries.ITEMS, namespace, path);
    }
    public static TagKey<Fluid> modFluidTag(String namespace, String path) {
        return modTag(ForgeRegistries.FLUIDS, namespace, path);
    }
    public static TagKey<Block> modBlockTag(String path) {
        return modTag(ForgeRegistries.BLOCKS, Metallurgica.ID, path);
    }
    public static TagKey<Item> modItemTag(String path) {
        return modTag(ForgeRegistries.ITEMS, Metallurgica.ID, path);
    }
    public static TagKey<Fluid> modFluidTag(String path) {
        return modTag(ForgeRegistries.FLUIDS, Metallurgica.ID, path);
    }
    
    public enum NameSpace {
        
        MOD(Metallurgica.ID, false, true),
        FORGE("forge"),
        TIC("tconstruct"),
        QUARK("quark"),
        TFMG("createindustry"),
        
        ;
        
        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;
        
        NameSpace(String id) {
            this(id, true, false);
        }
        
        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }
    
    public enum AllBlockTags {
        BAUXITE_ORE_REPLACEABLE,
        BUDDING_AMETHYST_REPLACEABLE,
        REVERBARATORY_GLASS,
        REVERBARATORY_WALL,
        REVERBARATORY_INPUT,
        DEPOSITS,
        AIR_BLOCKING,
        ;
        
        public final TagKey<Block> tag;
        public final boolean alwaysDatagen;
        
        AllBlockTags() {
            this(MOD);
        }
        
        AllBlockTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllBlockTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }
        
        AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.BLOCKS, id);
            } else {
                tag = BlockTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }
        
        @SuppressWarnings("deprecation")
        public boolean matches(Block block) {
            return block.builtInRegistryHolder()
                    .is(tag);
        }
        
        public boolean matches(ItemStack stack) {
            return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
        }
        
        public boolean matches(BlockState state) {
            return state.is(tag);
        }
        
        private static void init() {}
    }
    
    public enum AllItemTags {
        NEEDS_CHEMICAL_FORMULA_TOOLTIP,
        IGNITES_LOG_PILE,
        WATER_CONTAINERS,
        ;
        
        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;
        
        AllItemTags() {
            this(NameSpace.MOD);
        }
        
        AllItemTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllItemTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }
        
        AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.ITEMS, id);
            } else {
                tag = ItemTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }
        
        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }
        
        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }
        
        private static void init() {}
    }
    public enum AllFluidTags {
        REVERBARATORY_FUELS(MOD, "reverbaratory_fuels"),
        GAS(TFMG),
        CHLORIDE(MOD),
        ACID(MOD),
        ;
        
        public final TagKey<Fluid> tag;
        public final boolean alwaysDatagen;
        
        AllFluidTags() {
            this(NameSpace.MOD);
        }
        
        AllFluidTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllFluidTags(NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }
        
        AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }
        
        AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
            if (optional) {
                tag = optionalTag(ForgeRegistries.FLUIDS, id);
            } else {
                tag = FluidTags.create(id);
            }
            this.alwaysDatagen = alwaysDatagen;
        }
        
        @SuppressWarnings("deprecation")
        public boolean matches(Fluid fluid) {
            return fluid.is(tag);
        }
        
        public boolean matches(FluidState state) {
            return state.is(tag);
        }
        
        private static void init() {}
    }
    
    public static void init() {
        AllBlockTags.init();
        AllItemTags.init();
        AllFluidTags.init();
    }
}
