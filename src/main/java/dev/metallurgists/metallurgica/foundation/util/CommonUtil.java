package dev.metallurgists.metallurgica.foundation.util;

import dev.metallurgists.metallurgica.Metallurgica;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class CommonUtil {
    
    public static String getItemIdentifier(Item item) {
        String unwrappedHolder = ForgeRegistries.ITEMS.getHolder(item).flatMap(Holder::unwrapKey).toString();
        String prunedKey = unwrappedHolder.replace("Optional[ResourceKey[minecraft:item / ", "").replace("]]", "");
        return prunedKey;
    }
    
    public static String getFluidIdentifier(FluidStack fluid) {
        String unwrappedHolder = ForgeRegistries.FLUIDS.getHolder(fluid.getFluid()).flatMap(Holder::unwrapKey).toString();
        String prunedKey = unwrappedHolder.replace("Optional[ResourceKey[minecraft:fluid / ", "").replace("]]", "");
        return prunedKey;
    }
    
    public static ResourceLocation getItemIdentifierAsResourceLocation(Item item) {
        return new ResourceLocation(getItemIdentifier(item));
    }
    
    public static boolean isClassFound(String className) {
        try {
            Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static boolean isRainingAt(BlockPos pos, Level world) {
        if (!world.isRaining()) {
            return false;
        } else if (!world.canSeeSky(pos)) {
            return false;
        } else if (world.getHeight() > pos.getY()) {
            return false;
        } else {
            return true;
        }
    }
    
    public static MobEffectInstance noHeal(MobEffectInstance ei) {
        ei.setCurativeItems(ImmutableList.of());
        return ei;
    }

    public static float triangle(RandomSource random)
    {
        return random.nextFloat() - random.nextFloat() * 0.5f;
    }
    public static int triangle(RandomSource random, int range)
    {
        return random.nextInt(range) - random.nextInt(range);
    }
    public static float triangle(RandomSource random, float delta)
    {
        return (random.nextFloat() - random.nextFloat()) * delta;
    }
    public static double triangle(RandomSource random, double delta)
    {
        return (random.nextDouble() - random.nextDouble()) * delta;
    }

    public static BlockState readBlockState(CompoundTag tag) {
        if (!tag.contains("Name", 8)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            ResourceLocation resourcelocation = new ResourceLocation(tag.getString("Name"));
            Block block = BuiltInRegistries.BLOCK.get(ResourceKey.create(Registries.BLOCK, resourcelocation));
            if (block == null) {
                return Blocks.AIR.defaultBlockState();
            } else {
                BlockState blockstate = block.defaultBlockState();
                if (tag.contains("Properties", 10)) {
                    CompoundTag compoundtag = tag.getCompound("Properties");
                    StateDefinition<Block, BlockState> statedefinition = block.getStateDefinition();

                    for(String s : compoundtag.getAllKeys()) {
                        Property<?> property = statedefinition.getProperty(s);
                        if (property != null) {
                            blockstate = setValueHelper(blockstate, property, s, compoundtag, tag);
                        }
                    }
                }

                return blockstate;
            }
        }
    }

    private static <T extends Comparable<T>> BlockState setValueHelper(BlockState stateHolder, Property<T> property, String propertyName, CompoundTag propertiesTag, CompoundTag blockStateTag) {
        Optional<T> optional = property.getValue(propertiesTag.getString(propertyName));
        if (optional.isPresent()) {
            return stateHolder.setValue(property, optional.get());
        } else {
            Metallurgica.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", propertyName, propertiesTag.getString(propertyName), blockStateTag.toString());
            return stateHolder;
        }
    }
}
